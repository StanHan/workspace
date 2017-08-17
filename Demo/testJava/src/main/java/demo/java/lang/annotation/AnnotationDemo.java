package demo.java.lang.annotation;

import java.lang.annotation.Annotation;

import demo.vo.User;

public class AnnotationDemo {

    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }
    
    static void testAnnotation() throws Exception {
        System.out.println("HELLO");
        Annotation[] annotations = User.class.getAnnotations();
        for (Annotation annotation : annotations) {
            Class<?> _class = annotation.annotationType();
            System.out.println(_class.getName());
        }
    }

}
