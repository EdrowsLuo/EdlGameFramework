package com.edlplan.newosbplayer;

import com.edlplan.edlosbsupport.OsuStoryboard;
import com.edlplan.edlosbsupport.elements.StoryboardSprite;
import com.edlplan.edlosbsupport.parser.OsbFileParser;
import com.edlplan.edlosbsupport.player.OsbPlayer;
import com.edlplan.framework.Framework;
import com.edlplan.framework.MContext;
import com.edlplan.framework.graphics.opengl.BaseCanvas;
import com.edlplan.framework.graphics.opengl.BlendType;
import com.edlplan.framework.graphics.opengl.objs.texture.AutoPackTexturePool;
import com.edlplan.framework.graphics.opengl.objs.texture.TexturePool;
import com.edlplan.framework.main.EdlMainActivity;
import com.edlplan.framework.math.Color4;
import com.edlplan.framework.math.Vec2;
import com.edlplan.framework.media.bass.BassChannel;
import com.edlplan.framework.resource.BufferedListResource;
import com.edlplan.framework.resource.DirResource;
import com.edlplan.framework.test.performance.Tracker;
import com.edlplan.framework.ui.EdView;
import com.edlplan.framework.ui.ViewConfiguration;
import com.edlplan.framework.ui.additions.popupview.defviews.RenderStatPopupView;
import com.edlplan.framework.ui.layout.Gravity;
import com.edlplan.framework.ui.layout.MarginLayoutParam;
import com.edlplan.framework.ui.layout.Orientation;
import com.edlplan.framework.ui.layout.Param;
import com.edlplan.framework.ui.widget.EditText;
import com.edlplan.framework.ui.widget.Fragment;
import com.edlplan.framework.ui.widget.FrameLayout;
import com.edlplan.framework.ui.widget.LinearLayout;
import com.edlplan.framework.ui.widget.RelativeLayout;
import com.edlplan.framework.ui.widget.RoundedButton;
import com.edlplan.framework.ui.widget.TextView;

import java.io.File;
import java.io.IOException;

public class MainActivity extends EdlMainActivity {
    @Override
    protected void createGame() {
        initialWithView(GameView.class);
    }

    public static class GameView extends RelativeLayout {

        FrameLayout frameLayout;

        public GameView(MContext c) {
            super(c);
            children(
                    frameLayout = new FrameLayout(c) {{
                        layoutParam(
                                new RelativeParam() {{
                                    width = Param.MODE_MATCH_PARENT;
                                    height = Param.MODE_MATCH_PARENT;
                                }}
                        );
                    }}
            );

            frameLayout.setFragment(new SelectFragment());
        }

        @Override
        public void onInitialLayouted() {
            super.onInitialLayouted();
            RenderStatPopupView.getInstance(getContext()).show();
        }

        public void playStoryboard(String filename) {
            frameLayout.setFragment(new StoryboardFragment(filename));
        }

        public class StoryboardFragment extends Fragment {

            private String filename;

            public StoryboardFragment(String filename) {
                this.filename = filename;
            }

            @Override
            protected void onCreate(MContext c) {
                super.onCreate(c);


                File file = new File(
                        filename
                        //"/storage/emulated/0/osu!droid/Songs/大空直美 - DOKIDOKIリズム ～For Chieri rearrange MIX～/ohzora naomi - DOKIDOKI For Chieri rearrange MIX (Edrows Luo).osb"
                        //"/storage/emulated/0/osu!droid/Songs/108529 FujuniseikouyuuP - FREEDMAN/HujuniseikouyuuP - FREEDMAN (val0108).osb"
                        //"/storage/emulated/0/osu!droid/Songs/139525 Lite Show Magic (t+pazolite vs C-Show) - Crack Traxxxx/Lite Show Magic (t+pazolite vs C-Show) - Crack Traxxxx (Fatfan Kolek).osb"
                        //"/storage/emulated/0/osu!droid/Songs/470977 Mili - world.execute(me)/Mili - world.execute(me); (Exile-).osb"
                        //"/storage/emulated/0/osu!droid/Songs/582089 Camellia vs Akira Complex - Reality Distortion/Camellia vs Akira Complex - Reality Distortion (rrtyui).osb"
                        //"/storage/emulated/0/osu!droid/Songs/855677 Camellia - Exit This Earth's Atomosphere (Camellia's ''PLANETARY--200STEP'' Remix)/Camellia - Exit This Earth's Atomosphere (Camellia's ''PLANETARY200STEP'' Remix) (ProfessionalBox).osb"
                        //"/storage/emulated/0/osu!droid/Songs/s2:ginkiha - EOS/ginkiha - EOS (alacat).osb"
                        //"/storage/emulated/0/osu!droid/Songs/261911 MitiS & MaHi - Blu (Speed Up Ver.)/MitiS & MaHi - Blu (Speed Up Ver.) (Ashasaki).osb"
                        //"/storage/emulated/0/osu!droid/Songs/310499 Reol - Asymmetry/Reol - Asymmetry (Skystar).osb"
                        //"/storage/emulated/0/osu!droid/Songs/463479 Nanahira - Bassdrop Freaks (Long Ver.)/Nanahira - Bassdrop Freaks (Long Ver.) (yf_bmp).osb"
                );
                File dir = file.getParentFile();
                BufferedListResource resource = new BufferedListResource(new DirResource(dir));
                resource.setIgnoreCase(true);
                TexturePool pool = new TexturePool(s -> {
                    try {
                        return resource.loadTexture(s);
                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }
                });

                OsbContext context = new OsbContext();
                context.texturePool = pool;
                context.engines = new DepthOrderRenderEngine[StoryboardSprite.Layer.values().length];
                for (int i = 0; i < context.engines.length; i++) {
                    context.engines[i] = new DepthOrderRenderEngine();
                }

                OsbFileParser parser = new OsbFileParser(
                        file,
                        null);

                Tracker.createTmpNode("ParseOsb").wrap(() -> {
                    try {
                        parser.parse();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).then(System.out::println);
                OsuStoryboard storyboard = parser.getBaseParser().getStoryboard();

                pool.addAll(storyboard.getAllNeededTextures());
                AutoPackTexturePool autoPackTexturePool = new AutoPackTexturePool(s -> {
                    try {
                        return resource.loadTexture(s);
                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }
                }, c);
                autoPackTexturePool.addAll(pool.getAll());
                pool = autoPackTexturePool;
                context.texturePool = autoPackTexturePool;

                OsbPlayer osbPlayer = new OsbPlayer(s -> {
                    if (s.getClass() == StoryboardSprite.class) {
                        return new EGFStoryboardSprite(context);
                    } else {
                        return new EGFStoryboardAnimationSprite(context);
                    }
                });
                //osbPlayer.setPreLoad(true);

                System.out.println("Start load to play");
                Tracker.createTmpNode("LoadToPlaying").wrap(
                        () -> osbPlayer.loadStoryboard(storyboard)
                ).then(System.out::println);


                setContentView(new EdView(c) {
                    double startTime = -1;

                    BassChannel channel;

                    {
                        try {
                            String audio = null;
                            for (String s : resource.list("/")) {
                                if (s.endsWith(".mp3")) {
                                    audio = s;
                                    break;
                                }
                            }
                            if (audio != null) {
                                channel = BassChannel.createStreamFromResource(resource, audio);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }


                    @Override
                    protected void onDraw(BaseCanvas canvas) {
                        super.onDraw(canvas);
                        canvas.save();
                        float scale = Math.max(640 / canvas.getWidth(), 480 / canvas.getHeight());
                        Vec2 startOffset = new Vec2(canvas.getWidth() / 2, canvas.getHeight() / 2)
                                .minus(640 * 0.5f / scale, 480 * 0.5f / scale);

                        canvas.translate(startOffset.x, startOffset.y).expendAxis(scale);//.translate(64, 48);
                        if (startTime == -1) {
                            startTime = Framework.relativePreciseTimeMillion();
                            osbPlayer.start(0);
                            if (channel != null) {
                                channel.play();
                            }
                        } else {
                            double time = Framework.relativePreciseTimeMillion() - startTime;
                            osbPlayer.update(time);
                            for (DepthOrderRenderEngine engine : context.engines) {
                                engine.draw(canvas);
                            }
                        }
                        canvas.getBlendSetting().setBlendType(BlendType.Normal);
                        canvas.restore();
                    }
                });
            }
        }


        public class SelectFragment extends Fragment {

            EditText text;

            @Override
            protected void onCreate(final MContext context) {
                super.onCreate(context);


                setContentView(
                        new RelativeLayout(context) {{
                            children(
                                    new LinearLayout(context){{
                                        setPadding(ViewConfiguration.dp(15));
                                        setBackgroundColor(Color4.rgba(1, 1, 1, 0.3f));
                                        setOrientation(Orientation.DIRECTION_T2B);
                                        layoutParam(
                                                new RelativeParam() {{
                                                    width = Param.MODE_WRAP_CONTENT;
                                                    height = Param.MODE_WRAP_CONTENT;
                                                    gravity = Gravity.Center;
                                                }}
                                        );
                                        children(
                                                text = new EditText(context) {{
                                                    layoutParam(
                                                            new MarginLayoutParam() {{
                                                                width = Param.makeUpDP(350);
                                                                height = Param.MODE_WRAP_CONTENT;
                                                            }}
                                                    );
                                                }},
                                                new RoundedButton(context) {{
                                                    layoutParam(
                                                            new MarginLayoutParam() {{
                                                                marginTop = ViewConfiguration.dp(30);
                                                                width = Param.makeUpDP(200);
                                                                height = Param.makeUpDP(40);
                                                            }}
                                                    );
                                                    setOnClickListener(view -> playStoryboard(text.getTextView().getText()));
                                                    children(
                                                            new TextView(context) {{
                                                                layoutParam(
                                                                        new RelativeParam() {{
                                                                            width = Param.MODE_WRAP_CONTENT;
                                                                            height = Param.MODE_WRAP_CONTENT;
                                                                            gravity = Gravity.Center;
                                                                        }}
                                                                );
                                                                setTextSize(ViewConfiguration.dp(25));
                                                                setText("开始");
                                                            }}
                                                    );
                                                }}
                                        );
                                    }}
                            );
                        }}
                );
            }

        }
    }
}
