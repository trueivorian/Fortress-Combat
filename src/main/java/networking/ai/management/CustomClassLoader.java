package networking.ai.management;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.CodeSource;

class CustomClassLoader extends URLClassLoader {
    public CustomClassLoader() {
        super(new URL[0]);
    }

    protected java.lang.Class<?> findClass(String name)
            throws ClassNotFoundException {
        try{
            String c = name.replace('.', File.separatorChar) +".class";
            URL u = ClassLoader.getSystemResource(c);
            String classPath = ((String) u.getFile()).substring(1);
            File f = new File(classPath);

            FileInputStream fis = new FileInputStream(f);
            DataInputStream dis = new DataInputStream(fis);

            byte buff[] = new byte[(int) f.length()];
            dis.readFully(buff);
            dis.close();

            return defineClass(name, buff, 0, buff.length, (CodeSource) null);

        } catch(Exception e){
            throw new ClassNotFoundException(e.getMessage(), e);
        }
    }
}