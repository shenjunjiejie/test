package com.dfsdk.ocr.scan.network.request;

import java.util.ArrayList;
import java.util.List;

public class DFApiParameterList extends ArrayList<DFApiParameter> {
    private static final long serialVersionUID = 3668948424416187047L;

    private DFApiParameterList() {}

    public final Boolean add(String name, Object value) throws IllegalArgumentException {
        DFApiParameter parameter = new DFApiParameter(name, value);
        return this.add(parameter);
    }

    public final DFApiParameterList with(String name, Object value) throws IllegalArgumentException {
        this.add(name, value);
        return this;
    }

    public void remove(String name){
        for (DFApiParameter item : this) {
            if (item.name.equals(name)) {
                this.remove(item);
                break;
            }
        }
    }
    public void removeContains(String name){
        List<DFApiParameter> delete=new ArrayList<DFApiParameter>();
        for (DFApiParameter item : this) {
            if (item.name.startsWith(name)) {
                delete.add(item);
            }
        }
        for (DFApiParameter di : delete) {
            this.remove(di);
        }
    }
    public Object getValue(String name){
        Object ret=null;
        for (DFApiParameter item : this) {
            if (item.name.equals(name)) {
                ret =item.value;
                break;
            }
        }

        return ret;
    }
    public final static DFApiParameterList create() {
        return new DFApiParameterList();
    }

    public final static DFApiParameterList createWith(String name, Object value) throws IllegalArgumentException {
        DFApiParameterList list = new DFApiParameterList();
        return list.with(name, value);
    }

    public final boolean contains(String name){
        for (DFApiParameter item : this) {
            if (item.name.equals(name)) {
                return true;
            }
        }
        return false;
    }
}
