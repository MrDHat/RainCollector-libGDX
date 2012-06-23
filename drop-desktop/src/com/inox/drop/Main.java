package com.inox.drop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
   public static void main(String[] args) {
      LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
      cfg.title = "Drop";
      cfg.useGL20 = true;
      cfg.width = 800;
      cfg.height = 480;
      new LwjglApplication(new Drop(), cfg);
   }
}