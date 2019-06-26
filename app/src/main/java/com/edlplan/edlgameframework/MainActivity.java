package com.edlplan.edlgameframework;

import com.edlplan.framework.MContext;
import com.edlplan.framework.graphics.opengl.objs.Color4;
import com.edlplan.framework.main.EdlMainActivity;
import com.edlplan.framework.ui.layout.Gravity;
import com.edlplan.framework.ui.layout.Param;
import com.edlplan.framework.ui.widget.RelativeLayout;
import com.edlplan.framework.ui.widget.TextView;

public class MainActivity extends EdlMainActivity {

    @Override
    protected void createGame() {
        initialWithView(TestView.class);
    }

    public static class TestView extends RelativeLayout {

        public TestView(MContext c) {
            super(c);
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
