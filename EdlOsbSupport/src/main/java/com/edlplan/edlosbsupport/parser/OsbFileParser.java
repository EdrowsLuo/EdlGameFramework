package com.edlplan.edlosbsupport.parser;


import com.edlplan.edlosbsupport.OsuStoryboard;
import com.edlplan.framework.utils.CharArray;
import com.edlplan.framework.utils.U;
import com.edlplan.framework.utils.io.AdvancedBufferedReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 预处理变量，识别Tag，处理注释
 */
public class OsbFileParser {

    private OsbBaseParser baseParser;

    private File file;

    private boolean parsed = false;

    private HashMap<String, String> variables = new HashMap<>();

    public OsbFileParser(File file, OsbBaseParser baseParser) {
        this.file = file;
        this.baseParser = baseParser == null ? new OsbBaseParser(new OsuStoryboard()) : baseParser;
    }

    public OsbBaseParser getBaseParser() {
        return baseParser;
    }

    public void parse() throws OsbParseException {
        if (parsed) {
            return;
        }

        try {
            System.gc();
            BufferedReader reader = new BufferedReader(new FileReader(file));
            List<String> varlines = new ArrayList<>();
            List<String> eventslines = new ArrayList<>((int) file.length() / 10);
            List<String> currentPart = null;
            String line;
            while ((line = reader.readLine()) != null) {
                String tr = line.trim();
                if (tr.length() == 0) {
                    continue;
                }
                if (tr.startsWith("//")) {
                    continue;
                }
                if (tr.charAt(0) == '[') {
                    switch (tr.charAt(1)) {
                        case 'V':
                            currentPart = varlines;
                            break;
                        case 'E':
                            currentPart = eventslines;
                            break;
                        default:
                            currentPart = null;
                    }
                    continue;
                }
                if (currentPart != null) {
                    currentPart.add(line);
                }
            }

            for (String v : varlines) {
                String[] spl = v.split("=");
                if (spl.length >= 2) {
                    variables.put(spl[0].trim(), spl[1].trim());
                }
            }
            varlines.clear();
            StringBuilder eventsString = new StringBuilder();
            for (String l : eventslines) {
                int loops = 0;
                while (l.indexOf('$') >= 0) {
                    String origin = l;
                    for (Map.Entry<String, String> v : variables.entrySet()) {
                        l = l.replace(v.getKey(), v.getValue());
                    }
                    if (l.equals(origin)) {
                        break;
                    }
                    loops++;
                    if (loops > 20) {
                        throw new OsbParseException("maybe a endless loop QwQ");
                    }
                }
                eventsString.append(l).append('\n');
            }
            eventslines.clear();
            System.gc();
            CharArray eventsCharArray = new CharArray(eventsString.toString());
            eventsString = null;
            System.gc();
            if (eventsCharArray.length() > 0) {
                eventsCharArray.end--; //减去末尾的空行
            }

            CharArray.CharArraySplitIterator spl = eventsCharArray.split('\n');
            spl.setAutoCache(true);
            while (spl.hasNext()) {
                baseParser.appendLine(spl.next());
            }
            spl.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        parsed = true;
    }

}
