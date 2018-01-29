package demo.javassist;

import demo.spring.service.impl.UserDAO;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.Translator;

/**
 *
 */
public class TranslatorDemo implements Translator {

    public static void main(String[] args) {
        UserDAO b = new UserDAO();
        b.findUserById(" 历史人物 ");
    }

    public void start(ClassPool pool) throws NotFoundException, CannotCompileException {
    }

    /**
     * 类装载到JVM前进行代码织入
     */
    public void onLoad(ClassPool classPool, String classname) {
        if (!"demo.spring.service.impl.UserDAO".equals(classname)) {
            return;
        }
        // 通过获取类文件
        try {
            CtClass ctClass = classPool.get(classname);
            // 获得指定方法名的方法
            CtMethod ctMethod = ctClass.getDeclaredMethod("findUserById");
            // 在方法执行前插入代码
            ctMethod.insertBefore("{ System.out.println(\"记录日志++++++++++\"); }");
        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (CannotCompileException e) {
            e.printStackTrace();
        }
    }

}