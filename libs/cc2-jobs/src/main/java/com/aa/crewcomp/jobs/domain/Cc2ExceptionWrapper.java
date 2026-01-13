package com.aa.crewcomp.jobs.domain;

import java.io.PrintWriter;
import java.io.StringWriter;
import lombok.Data;

@Data
public class Cc2ExceptionWrapper {
    public Cc2ExceptionWrapper(Exception e) {
        this.message = e.getMessage();
        this.stackTrace = getStackTraceAsString(e);
    }

    public static String getStackTraceAsString(Throwable throwable) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        throwable.printStackTrace(printWriter);
        return stringWriter.toString();
    }

    private String message;
    private String stackTrace;
}