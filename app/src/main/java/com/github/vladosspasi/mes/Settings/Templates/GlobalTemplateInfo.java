package com.github.vladosspasi.mes.Settings.Templates;

public class GlobalTemplateInfo {

    private static Template template;

    public static Template getTemplate() {
        return template;
    }

    public static void setTemplate(Template template) {
        GlobalTemplateInfo.template = template;
    }

    public static void clear(){
        template.clear();
    }
}
