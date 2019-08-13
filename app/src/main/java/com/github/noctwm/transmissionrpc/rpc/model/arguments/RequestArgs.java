package com.github.noctwm.transmissionrpc.rpc.model.arguments;

import java.util.Arrays;
import java.util.List;

public class RequestArgs {

    private List<String> fields = null;

    private List<Long> ids = null;

    public RequestArgs() {
    }

    public RequestArgs(List<String> fields, List<Long> ids) {
        this.fields = fields;
        this.ids = ids;
    }

    public RequestArgs(String[] fields, List<Long> ids) {
        this.fields = Arrays.asList(fields);
        this.ids = ids;
    }

    public List<String> getFields() {
        return fields;
    }

    public void setFields(String[] fields) {
        this.fields = Arrays.asList(fields);
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }
}
