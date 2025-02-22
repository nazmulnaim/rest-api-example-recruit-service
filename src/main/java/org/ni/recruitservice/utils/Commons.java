package org.ni.recruitservice.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.ni.recruitservice.exception.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;


/**
 * Created by nazmul on 9/21/2018.
 */

public class Commons {
    private static Logger log = LoggerFactory.getLogger(Commons.class);

    /*private static InputStream propIS = null;
    private static Properties props = new Properties();

    static{
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        propIS = classloader.getResourceAsStream("application.properties");
        try {
            props.load(propIS);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    public static boolean isEmpty(List list) {
        if (list == null) {

            return true;
        }
        if (list.size() == 0) {

            return true;
        }

        return false;
    }

    public static boolean isEmpty(String string) {
        if (string == null) {

            return true;
        }
        if ("".equals(string)) {

            return true;
        }

        return false;
    }

    public static <S,T> T transformObject(S source, Class<T> targetClass) throws ApiException {
        try {
            if (source == null) {
                return null;
            }
            T newTargetObject = targetClass.newInstance();
            BeanUtils.copyProperties(source, newTargetObject);
            return newTargetObject;
        }catch (Exception ex){
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR.value(),ex);
        }
    }

    public static <S,T> List<T> transformObjectList(List<S> sourceList, Class<T> targetClass) throws ApiException {
        try {
            List<T> listToReturn = new ArrayList<>();
            if(!isEmpty(sourceList)){
                for(Object sourceObject : sourceList){
                    listToReturn.add(transformObject(sourceObject, targetClass));
                }
            }
            return listToReturn;
        }catch (Exception ex){
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR.value(),ex);
        }
    }

    public static <T> T getObjectFromJsonString(String jsonStr, Class<T> targetClass) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(jsonStr, targetClass);
    }

    public static String objectToJsonString(Object object) throws Exception{
        if(null == object ){
            return "";
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }

    public static List getObjectMapperList(String clientResponseStr,  Class clz) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd");
        objectMapper.setDateFormat(simpleDateFormat);
        return objectMapper.readValue(
                clientResponseStr,
                objectMapper.getTypeFactory().constructCollectionType(
                        List.class, clz));
    }
}
