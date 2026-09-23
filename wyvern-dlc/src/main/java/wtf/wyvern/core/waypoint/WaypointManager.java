package wtf.wyvern.core.waypoint;

import wtf.wyvern.core.eventbus.EventManager;
import wtf.wyvern.core.eventbus.EventTarget;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.Generated;
import net.minecraft.util.math.RotationAxis;
import wtf.wyvern.Wyvern;
import wtf.wyvern.core.events.impl.render.EventHudRender;
import wtf.wyvern.core.font.Fonts;
import wtf.wyvern.utility.interfaces.IClient;
import wtf.wyvern.render.display.base.color.ColorRGBA;
import wtf.astroguard.J2C.FastNative;

public class WaypointManager implements IClient {
   private static final net.minecraft.util.Identifier ARROW_TEXTURE = Wyvern.id("icons/arrow.png");
   private static final ColorRGBA PLAYER_ARROW_COLOR = new ColorRGBA(255, 32, 32);
   private Waypoint activeWaypoint = null;
   private Waypoint activePlayerWaypoint = null;
   private final List<Waypoint> waypoints = new ArrayList<>();
   private final File storageFile;

   public WaypointManager() {
      this.storageFile = new File(Wyvern.DIRECTORY, "waypoints.json");
      this.load();
      EventManager.register(this);
   }

   @FastNative
   public void add(String name, double x, double y, double z) {
      this.waypoints.add(new Waypoint(name, x, y, z, this.currentDimension()));
      this.save();
   }

   @FastNative
   public void remove(Waypoint waypoint) {
      this.waypoints.remove(waypoint);
      if (this.activeWaypoint == waypoint) this.activeWaypoint = null;
      if (this.activePlayerWaypoint == waypoint) this.activePlayerWaypoint = null;
      this.save();
   }

   @FastNative
   public List<Waypoint> getWaypoints() {
      return Collections.unmodifiableList(this.waypoints);
   }

   public List<Waypoint> getWaypointsForCurrentDimension() {
      String dimension = this.currentDimension();
      return this.waypoints.stream().filter(point -> point.getDimension().isEmpty() || point.getDimension().equals(dimension)).toList();
   }

   @FastNative
   public void save() {
      JsonArray array = new JsonArray();
      for (Waypoint waypoint : this.waypoints) {
         JsonObject object = new JsonObject();
         object.addProperty("name", waypoint.getName());
         object.addProperty("x", waypoint.getX());
         object.addProperty("y", waypoint.getY());
         object.addProperty("z", waypoint.getZ());
         object.addProperty("dimension", waypoint.getDimension());
         array.add(object);
      }
      try (FileWriter writer = new FileWriter(this.storageFile)) {
         writer.write(array.toString());
      } catch (IOException ignored) {
      }
   }

   @FastNative
   private void load() {
      if (!this.storageFile.isFile()) return;
      try (FileReader reader = new FileReader(this.storageFile)) {
         JsonArray array = JsonParser.parseReader(reader).getAsJsonArray();
         for (int i = 0; i < array.size(); i++) {
            JsonObject object = array.get(i).getAsJsonObject();
            this.waypoints.add(new Waypoint(object.get("name").getAsString(), object.get("x").getAsDouble(), object.get("y").getAsDouble(), object.get("z").getAsDouble(), object.has("dimension") ? object.get("dimension").getAsString() : ""));
         }
      } catch (Exception ignored) {
      }
   }

   @FastNative
   private String currentDimension() {
      return mc.world == null ? "" : mc.world.getRegistryKey().getValue().toString();
   }

   @FastNative
   public void set(Waypoint waypoint) {
      this.activeWaypoint = waypoint;
   }

   @FastNative
   public void removeActive(Waypoint waypoint) {
      if (this.activeWaypoint != null && this.activeWaypoint.equals(waypoint)) {
         this.activeWaypoint = null;
      }

   }

   @FastNative
   public void clear() {
      this.activeWaypoint = null;
   }

   @FastNative
   public boolean isEmpty() {
      return this.activeWaypoint == null;
   }

   @FastNative
   public void setPlayerWaypoint(Waypoint waypoint) {
      this.activePlayerWaypoint = waypoint;
   }

   @FastNative
   public void removePlayerWaypoint(Waypoint waypoint) {
      if (this.activePlayerWaypoint != null && this.activePlayerWaypoint.equals(waypoint)) {
         this.activePlayerWaypoint = null;
      }

   }

   @FastNative
   public void clearPlayerWaypoint() {
      this.activePlayerWaypoint = null;
   }

   @FastNative
   public boolean isEmptyPlayerWaypoint() {
      return this.activePlayerWaypoint == null;
   }

   @FastNative
   @EventTarget
   public void onHUD(EventHudRender e) {
      if (mc.player != null) {
         float x;
         float y;
         float size;
         double x2;
         double z2;
         int distance;
         float yaw;
         if (this.activeWaypoint != null) {
            x = (float)mc.getWindow().getScaledWidth() / 2.0F;
            y = (float)mc.getWindow().getScaledHeight() / 4.0F;
            size = 16.0F;
            e.getContext().getMatrices().push();
            x2 = this.activeWaypoint.getX() - mc.player.getX();
            z2 = this.activeWaypoint.getZ() - mc.player.getZ();
            distance = (int)Math.sqrt(x2 * x2 + z2 * z2);
            yaw = (float)(-(Math.atan2(x2, z2) * 57.29577951308232D)) - mc.gameRenderer.getCamera().getYaw();
            String distText = distance + "m";
            e.getContext().drawText(Fonts.REGULAR.getFont(7.0F), distText, x - Fonts.REGULAR.getFont(7.0F).width(distText) / 2.0F, y + 8.0F, ColorRGBA.WHITE);
            e.getContext().getMatrices().translate(x, y, 0.0F);
            e.getContext().getMatrices().multiply(RotationAxis.POSITIVE_Z.rotationDegrees(yaw));
            e.getContext().getMatrices().translate(-x, -y, 0.0F);
            e.getContext().drawTexture(ARROW_TEXTURE, x - size / 2.0F, y - size / 2.0F, size, size, Wyvern.getInstance().getThemeManager().getCurrentTheme().getColor());
            e.getContext().getMatrices().pop();
         }

         if (this.activePlayerWaypoint != null) {
            x = (float)mc.getWindow().getScaledWidth() / 2.0F;
            y = (float)mc.getWindow().getScaledHeight() / 3.35F;
            size = 16.0F;
            e.getContext().getMatrices().push();
            x2 = this.activePlayerWaypoint.getX() - mc.player.getX();
            z2 = this.activePlayerWaypoint.getZ() - mc.player.getZ();
            distance = (int)Math.sqrt(x2 * x2 + z2 * z2);
            yaw = (float)(-(Math.atan2(x2, z2) * 57.29577951308232D)) - mc.gameRenderer.getCamera().getYaw();
            String playerDistText = distance + "m";
            e.getContext().drawText(Fonts.REGULAR.getFont(7.0F), this.activePlayerWaypoint.getName(), x - Fonts.REGULAR.getFont(7.0F).width(this.activePlayerWaypoint.getName()) / 2.0F, y + 7.0F, ColorRGBA.WHITE);
            e.getContext().drawText(Fonts.REGULAR.getFont(7.0F), playerDistText, x - Fonts.REGULAR.getFont(7.0F).width(playerDistText) / 2.0F, y + 14.0F, ColorRGBA.WHITE);
            e.getContext().getMatrices().translate(x, y, 0.0F);
            e.getContext().getMatrices().multiply(RotationAxis.POSITIVE_Z.rotationDegrees(yaw));
            e.getContext().getMatrices().translate(-x, -y, 0.0F);
            e.getContext().drawTexture(ARROW_TEXTURE, x - size / 2.0F, y - size / 2.0F, size, size, PLAYER_ARROW_COLOR);
            e.getContext().getMatrices().pop();
         }

      }
   }

   @Generated
   public Waypoint getActiveWaypoint() {
      return this.activeWaypoint;
   }

   @Generated
   public Waypoint getActivePlayerWaypoint() {
      return this.activePlayerWaypoint;
   }
}
