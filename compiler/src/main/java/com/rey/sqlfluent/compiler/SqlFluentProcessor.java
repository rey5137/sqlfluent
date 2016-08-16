package com.rey.sqlfluent.compiler;

import com.google.auto.common.SuperficialValidation;
import com.google.auto.service.AutoService;
import com.rey.sqlfluent.annotation.Column;
import com.rey.sqlfluent.annotation.Model;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeName;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.annotation.Annotation;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

import static javax.tools.Diagnostic.Kind.ERROR;
import static javax.tools.Diagnostic.Kind.NOTE;

@AutoService(Processor.class)
public final class SqlFluentProcessor extends AbstractProcessor {

    private Types typeUtils;
    private Filer filer;
    private Elements elementUtils;

    public static SqlFluentProcessor instance;

    @Override
    public synchronized void init(ProcessingEnvironment env) {
        super.init(env);
        instance = this;
        elementUtils = env.getElementUtils();
        typeUtils = env.getTypeUtils();
        filer = env.getFiler();
    }

    @Override public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        for (Class<? extends Annotation> annotation : getSupportedAnnotations()) {
            types.add(annotation.getCanonicalName());
        }
        return types;
    }

    private Set<Class<? extends Annotation>> getSupportedAnnotations() {
        Set<Class<? extends Annotation>> annotations = new LinkedHashSet<>();
        annotations.add(Model.class);
        annotations.add(Column.class);
        return annotations;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Map<TypeElement, ModelClass> targetClassMap = findAndParseTargets(roundEnv);

        for (Map.Entry<TypeElement, ModelClass> entry : targetClassMap.entrySet()) {
            TypeElement typeElement = entry.getKey();
            ModelClass modelClass = entry.getValue();

            for (JavaFile javaFile : modelClass.brewJava()) {
                try {
                    javaFile.writeTo(filer);
                } catch (IOException e) {
                    error(typeElement, "Unable to write view binder for type %s: %s", typeElement,
                            e.getMessage());
                }
            }
        }

        return true;
    }

    private Map<TypeElement, ModelClass> findAndParseTargets(RoundEnvironment env) {
        Map<TypeElement, ModelClass> targetClassMap = new LinkedHashMap<>();

        // Process each @Column element.
        for (Element element : env.getElementsAnnotatedWith(Column.class)) {
            debug("parse: " + element.toString());

            if (!SuperficialValidation.validateElement(element)) continue;
            try {
                parseColumnField(element, targetClassMap);
            } catch (Exception e) {
                logParsingError(element, Column.class, e);
            }
        }

        return targetClassMap;
    }

    private void parseColumnField(Element element, Map<TypeElement, ModelClass> targetClassMap){
        if(element.getModifiers().contains(Modifier.STATIC) || element.getModifiers().contains(Modifier.PRIVATE) || element.getModifiers().contains(Modifier.FINAL))
            throw new IllegalArgumentException("@" + Column.class.getSimpleName() + " cannot be annotated to private, static or final field.");

        TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();

        String fieldName = element.getSimpleName().toString();
        String columnName = element.getAnnotation(Column.class).value();
        TypeName fieldType = TypeName.get(element.asType());
        if(columnName == null || columnName.length() == 0)
            columnName = fieldName;
        ColumnField columnField = new ColumnField(fieldName, columnName, fieldType);

        ModelClass modelClass = getOrCreateTargetClass(targetClassMap, enclosingElement);
        modelClass.addColumnField(columnField);
    }

    private ModelClass getOrCreateTargetClass(Map<TypeElement, ModelClass> targetClassMap, TypeElement enclosingElement) {
        ModelClass modelClass = targetClassMap.get(enclosingElement);
        if (modelClass == null) {
            String packageName = getPackageName(enclosingElement);
            String className = getClassName(enclosingElement, packageName);
            ClassName modelClassName = ClassName.get(packageName, className);
            ClassName indexClassName = ClassName.get(packageName, className + "Index");
            ClassName queryClassName = ClassName.get(packageName, className + "Query");

            modelClass = new ModelClass(modelClassName, indexClassName, queryClassName);
            targetClassMap.put(enclosingElement, modelClass);
        }
        return modelClass;
    }

    private String getPackageName(TypeElement type) {
        return elementUtils.getPackageOf(type).getQualifiedName().toString();
    }

    private static String getClassName(TypeElement type, String packageName) {
        int packageLen = packageName.length() + 1;
        return type.getQualifiedName().toString().substring(packageLen).replace('.', '$');
    }

    public void debug(String message) {
        processingEnv.getMessager().printMessage(NOTE, message);
    }

    private void error(Element element, String message, Object... args) {
        if (args.length > 0)
            message = String.format(message, args);
        processingEnv.getMessager().printMessage(ERROR, message, element);
    }

    private void logParsingError(Element element, Class<? extends Annotation> annotation, Exception e) {
        StringWriter stackTrace = new StringWriter();
        e.printStackTrace(new PrintWriter(stackTrace));
        error(element, "Unable to parse @%s.\n\n%s", annotation.getSimpleName() + " " + e.toString(), stackTrace);
    }

}
