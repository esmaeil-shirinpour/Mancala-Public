package com.bol.mancala.exception;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.bol.mancala.util.Assert.has;

public class MancalaException extends RuntimeException implements Serializable {

    private String summary;
    private String detail;
    private List<MancalaException> exceptionList = new ArrayList<>();


    public MancalaException() {
    }

    public MancalaException(Throwable cause) {
        super(cause);
    }

    public MancalaException(String detail) {
        super(detail);
        this.detail = detail;
    }


    public MancalaException(String summary, String detail) {
        super(summary);
        this.detail = detail;
        this.summary = summary;
    }

    public String getDetail() {
        return detail;
    }

    public MancalaException setDetail(String detail) {
        this.detail = detail;
        return this;
    }

    public String getSummary() {
        return summary;
    }

    public MancalaException setSummary(String summary) {
        this.summary = summary;
        return this;
    }

    public List<MancalaException> getExceptionList() {
        return exceptionList;
    }

    public MancalaException setExceptionList(List<MancalaException> exceptionList) {
        this.exceptionList = exceptionList;
        return this;
    }

    public MancalaException addException(MancalaException be){
        exceptionList.add(be);
        return this;
    }

    public void build() {
        if(has(summary) || has(detail) || has(exceptionList)) {
            throw this;
        }
    }
}
