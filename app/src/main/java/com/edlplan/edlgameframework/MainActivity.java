package com.edlplan.edlgameframework;

import com.edlplan.edlosbsupport.OsuStoryboard;
import com.edlplan.edlosbsupport.OsuStoryboardLayer;
import com.edlplan.edlosbsupport.elements.StoryboardSprite;
import com.edlplan.edlosbsupport.parser.OsbFileParser;
import com.edlplan.edlosbsupport.parser.OsbParseException;
import com.edlplan.edlosbsupport.player.OsbPlayer;
import com.edlplan.framework.Framework;
import com.edlplan.framework.MContext;
import com.edlplan.framework.graphics.opengl.BaseCanvas;
import com.edlplan.framework.graphics.opengl.BlendType;
import com.edlplan.framework.graphics.opengl.batch.v2.object.FlippableTextureQuad;
import com.edlplan.framework.graphics.opengl.batch.v2.object.TextureQuadBatch;
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
import com.edlplan.framework.ui.additions.popupview.defviews.RenderStatPopupView;
import com.edlplan.framework.ui.layout.Gravity;
import com.edlplan.framework.ui.layout.Param;
import com.edlplan.framework.ui.widget.RelativeLayout;
import com.edlplan.framework.ui.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
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

            File file = new File(
                    //"/storage/emulated/0/osu!droid/Songs/大空直美 - DOKIDOKIリズム ～For Chieri rearrange MIX～/ohzora naomi - DOKIDOKI For Chieri rearrange MIX (Edrows Luo).osb"
                    //"/storage/emulated/0/osu!droid/Songs/108529 FujuniseikouyuuP - FREEDMAN/HujuniseikouyuuP - FREEDMAN (val0108).osb"
                    //"/storage/emulated/0/osu!droid/Songs/139525 Lite Show Magic (t+pazolite vs C-Show) - Crack Traxxxx/Lite Show Magic (t+pazolite vs C-Show) - Crack Traxxxx (Fatfan Kolek).osb"
                    "/storage/emulated/0/osu!droid/Songs/470977 Mili - world.execute(me)/Mili - world.execute(me); (Exile-).osb"
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

            //Tracker.createTmpNode("ParseOsb").wrap(() -> {
            try {
                parser.parse();

            } catch (Exception e) {
                e.printStackTrace();
            }
            //}).then(System.out::println);
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

            System.out.println("Start load to play");
            Tracker.createTmpNode("LoadToPlaying").wrap(
                    () -> osbPlayer.loadStoryboard(storyboard)
            ).then(System.out::println);

            children(
                    new EdView(c) {
                        double startTime = -1;

                        BassChannel channel;

                        {
                            try {
                                channel = BassChannel.createStreamFromResource(resource, "audio.mp3");
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

                            canvas.translate(startOffset.x, startOffset.y).expendAxis(scale * 2);//.translate(64, 48);
                            if (startTime == -1) {
                                startTime = Framework.relativePreciseTimeMillion();
                                osbPlayer.start(0);
                                channel.play();
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

                        @Override
                        public void onInitialLayouted() {
                            super.onInitialLayouted();
                            RenderStatPopupView.getInstance(getContext()).show();
                        }
                    }
            );
        }

    }

}
