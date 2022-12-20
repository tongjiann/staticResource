import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author xiwang
 * @apiNote
 * @since 2022-12-20 15:14
 */
public class CreateDir {

    public static final List<String> FILTERED_DIR = Arrays.asList(
            "target",
            ".gitignore",
            ".git",
            ".idea",
            ".pom",
            ".xml",
            "CreateDir",
            "README.md",
            "LICENSE",
            ".DS_Store"
    );

    public static void main(String[] args) throws IOException {
        StringBuilder sb = new StringBuilder();

        File file = new File(".");
        List<String> projNameList = new ArrayList<>();
        cycleFile(file, sb, 0, null, projNameList);
        File mdFile = new File("./README.md");
        if (!mdFile.exists()) {
            mdFile.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream(mdFile);
        fos.write(buildCatalogue(projNameList));
        fos.write(sb.toString().getBytes());
        fos.close();
        FileInputStream fis = new FileInputStream(mdFile);
        System.out.println(new String(fis.readAllBytes()));
        fis.close();
    }

    private static byte[] buildCatalogue(List<String> projNameList) {
        StringBuilder sb = new StringBuilder();
        sb.append("# 目录\n");
        projNameList.forEach(projName -> sb.append("- [" + projName + "](#" + replaceProjName(projName) + ")\n"));
        sb.append("\n---\n---\n").append("# 项目\n");
        return sb.toString().getBytes();
    }

    private static String replaceProjName(String projName) {
        List<String> list = Arrays.asList(".", "@");
        for (String s : list) {
            if (projName.contains(s)) {
                projName = projName.replace(s, "");
            }
        }
        return projName.toLowerCase();
    }

    private static void cycleFile(final File file, final StringBuilder sb, int level, String dirName, List<String> projNameList) {
        String fileName = file.getName();
        if (FILTERED_DIR.stream().anyMatch(fileName::contains)) {
            return;
        }
        if (file.isFile()) {
            sb.append("- [").append(fileName).append("](").append(dirName).append("/").append(fileName).append(")\n");
            return;
        }
        for (int i = 0; i < level; i++) {
            sb.append("#");
        }
        if (!".".equals(fileName)) {
            sb.append(" ").append(fileName).append("\n");
        }
        File[] a = file.listFiles();
        List<File> files = Arrays.asList(a);
        if (files.isEmpty()) {
            return;
        }
        if (level == 1) {
            projNameList.add(fileName);
        }
        files.remove(file);
        files.forEach(f -> cycleFile(f, sb, level + 1, dirName == null ? "." : dirName + "/" + fileName, projNameList));
    }
}
