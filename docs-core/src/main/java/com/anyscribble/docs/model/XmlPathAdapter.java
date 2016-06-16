package com.anyscribble.docs.model;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.nio.file.Path;
import java.nio.file.Paths;


public class XmlPathAdapter extends XmlAdapter<String, Path> {

    @Override
    public Path unmarshal(String v) throws Exception {
        return Paths.get(v);
    }

    @Override
    public String marshal(Path v) throws Exception {
        return v.toString();
    }
}
