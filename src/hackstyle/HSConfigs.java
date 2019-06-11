package hackstyle;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class HSConfigs {

    private final String fileName;
    private final Map<String, String> configs;

    private boolean changed = false;

    public HSConfigs(String fileName) {
        this.fileName = fileName;
        this.configs = new LinkedHashMap<>();
    }

    public void loadData() {
        List<String> list = new LinkedList<>();
        try (FileReader reader = new FileReader(new File(fileName))) {
            int length;
            final char[] array = new char[128];
            StringBuilder builder = new StringBuilder();
            while ((length = reader.read(array)) != -1) {
                builder.append(new String(array, 0, length));
            }
            Collections.addAll(list, builder.toString().split("\n"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (String string : list) {
            String[] s = string.split(";");
            if (s.length == 2) {
                configs.put(s[0], s[1]);
            }
        }
    }

    public int getIndicator(String name) {
        return KeyboardUtils.parseCharToKey(getString(name).charAt(0), 0);
    }

    public int getInt(String name) {
        return Integer.parseInt(getString(name));
    }

    public String getString(String name) {
        return configs.getOrDefault(name, " ");
    }

    public void set(String property, String value) {
        changed = true;
        configs.put(property, value);
    }

    public void set(String property, int value) {
        changed = true;
        configs.put(property, "" + value);
    }

    public void updateFile() {
        if (changed) {
            try (FileWriter writer = new FileWriter(new File(fileName), false)) {
                DateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                writer.write("Last updated: " + format.format(new Date()) + "\n");

                for (Map.Entry<String, String> property : configs.entrySet()) {
                    writer.write(property.getKey() + ";" + property.getValue() + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
