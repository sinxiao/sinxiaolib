package com.sinxiao.pak.annation;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

@AutoService(Processor.class)
public class TestProcess extends AbstractProcessor {
    private Filer filer;
    private Types typeUtils;
    private Elements elementUtils;
    private Messager messager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        filer = processingEnvironment.getFiler();
        typeUtils = processingEnvironment.getTypeUtils();
        elementUtils = processingEnvironment.getElementUtils();
        messager = processingEnvironment.getMessager();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        TypeSpec nowClass = TypeSpec.classBuilder("MyActivityClass")
                .addModifiers(Modifier.PUBLIC).build();
        JavaFile javaFile = JavaFile.builder("com.sinxiao.pak.annation", nowClass).build();
        try {
            javaFile.writeTo(filer);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> linkSet = new LinkedHashSet<>();
        linkSet.add(Override.class.getCanonicalName());
        return linkSet;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_7;
    }
}
