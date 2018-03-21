package demo.javax.tools;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Arrays;

import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

/**
 * 编译器
 *
 */
public class CompilerTest {
	public static void main(String[] args) throws Exception {

	}

	/*public static void test3(InputStream is) {
		ClassReader classReader = new ClassReader(is);
		ClassNode classNode = new ClassNode();
		classReader.accept(classNode, 0);
		for (Object object : classNode.methods) {
			MethodNode mn = (MethodNode) object;
			if ("<init>".equals(mn.name) || "<clinit>".equals(mn.name)) {
				continue;
			}
			InsnList insns = mn.instructions;
			InsnList il = new InsnList();
			il.add(new FieldInsnNode(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;"));
			il.add(new LdcInsnNode("Enter method -> " + mn.name));
			il.add(new MethodInsnNode(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V"));
			insns.insert(il);
			mn.maxStack += 3;
		}
		ClassWriter classWriter = new ClassWriter(0);
		classNode.accept(classWriter);
		byte[] b = classWriter.toByteArray();
	}*/

	/*private static double calculate(String expr) throws Exception  {
		   String className = "CalculatorMain";
		   String methodName = "calculate";
		   String source = "public class " + className 
		      + " { public static double " + methodName + "() { return " + expr + "; } }";
			  //省略动态编译Java源代码的相关代码，参见上一节
		   boolean result = task.call();
		   if (result) {
		      ClassLoader loader = Calculator.class.getClassLoader(); 
		      try {            
		         Class<?> clazz = loader.loadClass(className);
		         Method method = clazz.getMethod(methodName, new Class<?>[] {});
		         Object value = method.invoke(null, new Object[] {});
		         return (Double) value;
		      } catch (Exception e) {
		         throw new Exception("内部错误。");        
		      }    
		   } else {
		      throw new Exception("错误的表达式。");    
		   }
		}*/

	public static void test2() {
		String source = "public class Main { public static void main(String[] args) {System.out.println(\"Hello World!\");} }";
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
		JavaSourceFromString sourceObject = new JavaSourceFromString("Main", source);
		Iterable<? extends SimpleJavaFileObject> fileObjects = Arrays.asList(sourceObject);
		CompilationTask task = compiler.getTask(null, fileManager, null, null, null, fileObjects);
		boolean result = task.call();
		if (result) {
			System.out.println("编译成功。");
		}
	}

	public static void test1() throws IOException {
		File[] files1 = {}; // input for first compilation task
		File[] files2 = {}; // input for second compilation task

		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);

		Iterable<? extends JavaFileObject> compilationUnits1 = fileManager
				.getJavaFileObjectsFromFiles(Arrays.asList(files1));

		compiler.getTask(null, fileManager, null, null, null, compilationUnits1).call();
		// use alternative method

		Iterable<? extends JavaFileObject> compilationUnits2 = fileManager.getJavaFileObjects(files2);
		// reuse the same file manager to allow caching of jar files
		compiler.getTask(null, fileManager, null, null, null, compilationUnits2).call();

		fileManager.close();
	}

	/*static class MethodEntryTransformer implements ClassFileTransformer {
		@Override
		public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
				ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
			try {
				ClassReader cr = new ClassReader(classfileBuffer);
				ClassNode cn = new ClassNode();
				// 省略使用ASM进行字节代码转换的代码
				ClassWriter cw = new ClassWriter(0);
				cn.accept(cw);
				return cw.toByteArray();
			} catch (Exception e) {
				return null;
			}
		}

	}*/
}

/**
 * A file object used to represent source coming from a string.
 */
class JavaSourceFromString extends SimpleJavaFileObject {
	/**
	 * The source code of this "file".
	 */
	final String code;

	/**
	 * Constructs a new JavaSourceFromString.
	 * 
	 * @param name
	 *            the name of the compilation unit represented by this file
	 *            object
	 * @param code
	 *            the source code for the compilation unit represented by this
	 *            file object
	 */
	JavaSourceFromString(String name, String code) {
		super(URI.create("string:///" + name.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);
		this.code = code;
	}

	@Override
	public CharSequence getCharContent(boolean ignoreEncodingErrors) {
		return code;
	}
}
