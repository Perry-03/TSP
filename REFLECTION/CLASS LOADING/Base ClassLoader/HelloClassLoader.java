import java.io.*;

public class HelloClassLoader extends ClassLoader {
    String[] directories;

    public HelloClassLoader(String path) { directories = path.split(";"); }
    public HelloClassLoader(String path, ClassLoader parent) {
        super(parent);
        directories = path.split(";");
    }

    public synchronized Class<?> findClass(String name) throws ClassNotFoundException {
        for (int i = 0; i < directories.length; i++) {
            byte[] buf = getClassData(directories[i], name);
            if (buf != null) return defineClass(name, buf, 0, buf.length);
        }
        throw new ClassNotFoundException();
    }

    private byte[] getClassData(String dir, String fileName) {
        String classFile = dir + "/" + fileName.replace('.', '/') + ".class";
        int classSize = (int) (new File(classFile)).length();
        byte[] buf = new byte[classSize];

        try {
            FileInputStream filein = new FileInputStream(classFile);
            classSize = filein.read(buf);
            filein.close();
        } catch (FileNotFoundException e) { 
            e.printStackTrace(); 
        } catch (IOException e) { e.printStackTrace(); }
        
        return buf;
    }
}