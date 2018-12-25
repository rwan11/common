package com.rwan.im.server.example.marshalling;

import io.netty.handler.codec.marshalling.*;
import org.jboss.marshalling.MarshallerFactory;
import org.jboss.marshalling.Marshalling;
import org.jboss.marshalling.MarshallingConfiguration;

/**
 * @author johnlog
 * @date 2018/12/25
 */
public class MarshallingCodeFactory {


    public static MarshallingDecoder createDecoder(){


//        MarshallerFactory factory = new SerialMarshallerFactory();
        MarshallerFactory factory = Marshalling.getProvidedMarshallerFactory("serial");
        MarshallingConfiguration configuration = new MarshallingConfiguration();
        configuration.setVersion(5);
        UnmarshallerProvider provider = new DefaultUnmarshallerProvider(factory,configuration);
        MarshallingDecoder decoder = new MarshallingDecoder(provider);

        return decoder;
    }


    public static MarshallingEncoder createEncoder(){

        MarshallerFactory factory = Marshalling.getProvidedMarshallerFactory("serial");

        MarshallingConfiguration configuration = new MarshallingConfiguration();
        configuration.setVersion(5);

        MarshallerProvider provider = new DefaultMarshallerProvider(factory,configuration);

        MarshallingEncoder encoder = new MarshallingEncoder(provider);

        return encoder;
    }
}
