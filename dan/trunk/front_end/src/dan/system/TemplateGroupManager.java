/*
 * TemplateGroupManager
 *
 * Copyright 2011, Alan Grover, All rights reserved
 */

package dan.system;

import org.antlr.stringtemplate.StringTemplateGroup;

/**
 * Provides global access to the template library.
 * @author Alan
 */
public class TemplateGroupManager {
    private static StringTemplateGroup templateLib;

    public static StringTemplateGroup getTemplateLib(){
        return templateLib;
    }
    public static void setTemplateLib(StringTemplateGroup t){
        if(templateLib == null){
            templateLib = t;
        }
        else {
            throw new UnsupportedOperationException("only one template group is allowed");
        }
    }
}
