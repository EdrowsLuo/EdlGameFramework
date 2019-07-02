package com.edlplan.edlgameframework;

import com.edlplan.edlosbsupport.OsuStoryboard;
import com.edlplan.edlosbsupport.parser.OsbFileParser;
import com.edlplan.edlosbsupport.parser.OsbParseException;
import com.edlplan.framework.MContext;
import com.edlplan.framework.main.EdlMainActivity;
import com.edlplan.framework.math.Color4;
import com.edlplan.framework.test.performance.Tracker;
import com.edlplan.framework.ui.layout.Gravity;
import com.edlplan.framework.ui.layout.Param;
import com.edlplan.framework.ui.widget.RelativeLayout;
import com.edlplan.framework.ui.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class MainActivity extends EdlMainActivity {

    @Override
    protected void createGame() {
        initialWithView(TestView.class);
    }

    public static class TestView extends RelativeLayout {

        public TestView(MContext c) {
            super(c);

            OsbFileParser parser = new OsbFileParser(
                    new File(
                            //"/storage/emulated/0/osu!droid/Songs/108529 FujuniseikouyuuP - FREEDMAN/HujuniseikouyuuP - FREEDMAN (val0108).osb"
                            //"/storage/emulated/0/osu!droid/Songs/139525 Lite Show Magic (t+pazolite vs C-Show) - Crack Traxxxx/Lite Show Magic (t+pazolite vs C-Show) - Crack Traxxxx (Fatfan Kolek).osb"
                            "/storage/emulated/0/osu!droid/Songs/470977 Mili - world.execute(me)/Mili - world.execute(me); (Exile-).osb"
                    ),
                    null);

            try {
                Tracker.createTmpNode("ParseOsb").wrap(()->{
                    try {
                        //parser.parse();
                        //ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("/sdcard/MyDisk/out.jobj"));
                        //objectOutputStream.writeObject(parser.getBaseParser().getStoryboard());
                        ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("/sdcard/MyDisk/out.jobj"));
                        OsuStoryboard storyboard = (OsuStoryboard) inputStream.readObject();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).then(System.out::println);
                //System.out.println(parser.getBaseParser().getStoryboard());
            } catch (Exception e) {
                e.printStackTrace();
            }

            children(
                    new TextView(c) {{
                        layoutParam(new RelativeParam() {{
                            width = Param.MODE_WRAP_CONTENT;
                            height = Param.MODE_WRAP_CONTENT;
                            gravity = Gravity.Center;
                        }});
                        setText("Test");
                        setTextColor(Color4.Black);
                        setTextSize(200);
                        setBackgroundColor(Color4.White);
                    }}
            );
        }

    }

}
