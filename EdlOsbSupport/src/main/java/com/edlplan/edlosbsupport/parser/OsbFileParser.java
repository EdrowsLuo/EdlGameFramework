package com.edlplan.edlosbsupport.parser;


import com.edlplan.edlosbsupport.OsuStoryboard;
import com.edlplan.framework.utils.CharArray;
import com.edlplan.framework.utils.U;
import com.edlplan.framework.utils.functionality.SmartIterator;
import com.edlplan.framework.utils.io.AdvancedBufferedReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
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
                            if (tr.charAt(2) == 'v') {
                                currentPart = eventslines;
                            } else {
                                currentPart = null;
                            }
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

            //System.out.println("to apply vars");
            //LinkedList<String> eventsString = new LinkedList<>();
            Iterator<String> iterator;// = eventsString.listIterator();
            if (variables.isEmpty()) {
                iterator = eventslines.iterator();
                //eventsString.addAll(eventslines);
            } else {
                iterator = SmartIterator.wrap(eventslines.iterator()).applyFunction(l -> {
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
                            throw new RuntimeException("maybe a endless loop QwQ");
                        }
                    }
                    return l;
                });
            }
            //eventslines.clear();
            System.gc();
            //System.out.println("to parse lines");

            CharArray ca = CharArray.getCachedObject();
            while (iterator.hasNext()) {
                ca.set(iterator.next(), 0, -1);
                baseParser.appendLine(ca);
            }
            System.gc();

        } catch (IOException e) {
            e.printStackTrace();
        }

        parsed = true;
    }

}
