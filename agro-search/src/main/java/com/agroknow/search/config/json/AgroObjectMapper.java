package com.agroknow.search.config.json;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.map.*;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.map.introspect.JacksonAnnotationIntrospector;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Component("jacksonObjectMapper")
public class AgroObjectMapper extends org.codehaus.jackson.map.ObjectMapper {

    public AgroObjectMapper() {
        super();
    }

    public AgroObjectMapper(JsonFactory jf) {
        super(jf);
    }

    public AgroObjectMapper(JsonFactory jf, SerializerProvider sp, DeserializerProvider dp) {
        super(jf, sp, dp);
    }

    public AgroObjectMapper(JsonFactory jf, SerializerProvider sp, DeserializerProvider dp, SerializationConfig sconfig, DeserializationConfig dconfig) {
        super(jf, sp, dp, sconfig, dconfig);
    }

    @PostConstruct
    public void init() {
        setup();
    }

    public void setup() {
        // enable annotation support
        final AnnotationIntrospector introspector = new JacksonAnnotationIntrospector();
        SerializationConfig serializationConfig = this.getSerializationConfig();
        serializationConfig = serializationConfig.withAnnotationIntrospector(introspector)
                                // write dates as timestamps
                                .without(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS)
                                // exclude empty fields
                                .withSerializationInclusion(Inclusion.NON_EMPTY);

        DeserializationConfig deserializationConfig = this.getDeserializationConfig();
        deserializationConfig = deserializationConfig.withAnnotationIntrospector(introspector)
                                .without(org.codehaus.jackson.map.DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);

        this.setSerializationConfig(serializationConfig);
        this.setDeserializationConfig(deserializationConfig);
    }

    public ObjectWriter getObjectWriter(Class<?> clazz) throws IOException {
        return this.writerWithView(clazz);
    }

}
