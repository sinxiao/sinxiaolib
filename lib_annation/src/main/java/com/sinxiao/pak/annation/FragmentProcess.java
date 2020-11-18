package com.sinxiao.pak.annation;

import com.google.auto.service.AutoService;
import com.sinxiao.pak.annation.utils.Constants;

import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;


// AutoService则是固定的写法，加个注解即可
// 通过auto-service中的@AutoService可以自动生成AutoService注解处理器，用来注册
// 用来生成 META-INF/services/javax.annotation.processing.Processor 文件
@AutoService(Processor.class)
// 允许/支持的注解类型，让注解处理器处理
@SupportedAnnotationTypes({Constants.AROUTER_ANNOTATION_TYPES})
// 指定JDK编译版本
@SupportedSourceVersion(SourceVersion.RELEASE_7)
// 注解处理器接收的参数
@SupportedOptions({Constants.MODULE_NAME, Constants.APT_PACKAGE})
public class FragmentProcess extends AbstractProcessor {
    private Elements elemenstUtils;
    private Types typesUtils;
    private Filer filer;
    private Messager messager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        elemenstUtils = processingEnvironment.getElementUtils();
        typesUtils = processingEnvironment.getTypeUtils();
        filer = processingEnvironment.getFiler();
        messager = processingEnvironment.getMessager();
        Map<String, String> map = processingEnvironment.getOptions();

    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        System.out.println("in proces ss ...");

        return false;
    }
}
