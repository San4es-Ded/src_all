package aethereal.config;

import aethereal.ambience.Ambience;
import aethereal.api.Compile;
import aethereal.autobuy.AutoBuyEntry;
import aethereal.command.CommandProcessor;
import aethereal.command.LayoutCommand;
import aethereal.core.EventTarget;
import aethereal.core.Module;
import aethereal.core.NativeMethodLookup;
import aethereal.core.Processor_2;
import aethereal.core.Westra;
import aethereal.event.KeyEvent;
import aethereal.lib.json.JSONArray;
import aethereal.lib.json.JSONObject;
import aethereal.module.combat.AimAssistant;
import aethereal.module.combat.AntiBot;
import aethereal.module.combat.Aura;
import aethereal.module.combat.AutoArmor;
import aethereal.module.combat.AutoEXP;
import aethereal.module.combat.AutoExplosion;
import aethereal.module.combat.AutoSwap;
import aethereal.module.combat.AutoTotem;
import aethereal.module.combat.Criticals;
import aethereal.module.combat.CrossCart;
import aethereal.module.combat.CrystalOptimizer;
import aethereal.module.combat.HitBoxes;
import aethereal.module.combat.HitSounds;
import aethereal.module.combat.ItemHelper;
import aethereal.module.combat.MaceHelper;
import aethereal.module.combat.NoFriendDamage;
import aethereal.module.combat.NoServerDesync;
import aethereal.module.combat.NoServerPack;
import aethereal.module.combat.NoSlotChange;
import aethereal.module.combat.ProjectileHelper;
import aethereal.module.combat.ShiftTAP;
import aethereal.module.combat.TapeMouse;
import aethereal.module.combat.TriggerBot;
import aethereal.module.combat.Velocity;
import aethereal.module.misc.AHHelper;
import aethereal.module.misc.AncientFarmer;
import aethereal.module.misc.AntiAFK;
import aethereal.module.misc.AntiCrash;
import aethereal.module.misc.AppleFarmer;
import aethereal.module.misc.AutoBuy;
import aethereal.module.misc.AutoCommands;
import aethereal.module.misc.AutoDuel;
import aethereal.module.misc.AutoEnd;
import aethereal.module.misc.AutoSell;
import aethereal.module.misc.AutoWarden;
import aethereal.module.misc.ChatHelper;
import aethereal.module.misc.ClanUpgrader;
import aethereal.module.misc.Collector_2;
import aethereal.module.misc.Communication;
import aethereal.module.misc.FunDeliver;
import aethereal.module.misc.KTLeave;
import aethereal.module.misc.MineAssistant;
import aethereal.module.misc.NoCommands;
import aethereal.module.misc.NoInteract;
import aethereal.module.misc.Nuker;
import aethereal.module.misc.Optimization;
import aethereal.module.misc.PortalBypass;
import aethereal.module.misc.PotionThrower;
import aethereal.module.misc.Profiler;
import aethereal.module.misc.SchematicRecorder;
import aethereal.module.misc.ServerAssistant;
import aethereal.module.misc.ServerJoiner;
import aethereal.module.misc.Sounds;
import aethereal.module.misc.StreamerMode;
import aethereal.module.misc.TotemTracker;
import aethereal.module.misc.XRay;
import aethereal.module.movement.AirStuck;
import aethereal.module.movement.AutoDodge;
import aethereal.module.movement.ElytraBooster;
import aethereal.module.movement.ElytraMotion;
import aethereal.module.movement.ElytraRecast;
import aethereal.module.movement.ElytraTarget;
import aethereal.module.movement.FastBreak;
import aethereal.module.movement.FastLadder;
import aethereal.module.movement.Fly;
import aethereal.module.movement.FreeCamera;
import aethereal.module.movement.NoCrouch;
import aethereal.module.movement.NoDelay;
import aethereal.module.movement.NoPush;
import aethereal.module.movement.NoSlowDown;
import aethereal.module.movement.SafeWalk;
import aethereal.module.movement.Scaffold;
import aethereal.module.movement.ScreenWalk;
import aethereal.module.movement.Speed;
import aethereal.module.movement.Sprint;
import aethereal.module.movement.Strafe;
import aethereal.module.movement.WallClimb;
import aethereal.module.movement.WaterJump;
import aethereal.module.movement.WindBoost;
import aethereal.module.player.AntiDrop;
import aethereal.module.player.ArmorNotifier;
import aethereal.module.player.AucReissue;
import aethereal.module.player.AutoAccept;
import aethereal.module.player.AutoAuth;
import aethereal.module.player.AutoEat;
import aethereal.module.player.AutoFish;
import aethereal.module.player.AutoLeave;
import aethereal.module.player.AutoRelease;
import aethereal.module.player.AutoRespawn;
import aethereal.module.player.AutoTool;
import aethereal.module.player.CaptchaSolver;
import aethereal.module.player.ChestStealer;
import aethereal.module.player.ClickAction;
import aethereal.module.player.ClickPearl;
import aethereal.module.player.Crafter;
import aethereal.module.player.DeathCoords;
import aethereal.module.player.ElytraHelper;
import aethereal.module.player.ElytraSwap;
import aethereal.module.player.FakeLags;
import aethereal.module.player.FastEXP;
import aethereal.module.player.FastLoad;
import aethereal.module.player.HandSwap;
import aethereal.module.player.ItemScroller;
import aethereal.module.player.LockSlot;
import aethereal.module.player.OpenWalls;
import aethereal.module.player.PvpSafe;
import aethereal.module.player.SoundReducer;
import aethereal.module.player.Structures;
import aethereal.module.player.ThirdPerson;
import aethereal.module.player.UseTracker;
import aethereal.module.player.WindHop;
import aethereal.module.player.WindJump;
import aethereal.module.render.Arrows;
import aethereal.module.render.AspectRatio;
import aethereal.module.render.Atmosphere;
import aethereal.module.render.BlockESP;
import aethereal.module.render.BlockOverlay;
import aethereal.module.render.BoardSpoofer;
import aethereal.module.render.BuildViewer;
import aethereal.module.render.Buttons;
import aethereal.module.render.ChinaHat;
import aethereal.module.render.Crosshair;
import aethereal.module.render.CustomHotbar;
import aethereal.module.render.Emotions;
import aethereal.module.render.EntityBox;
import aethereal.module.render.EntityESP;
import aethereal.module.render.FakePlayer;
import aethereal.module.render.FreeLook;
import aethereal.module.render.FullBright;
import aethereal.module.render.HandsShader;
import aethereal.module.render.HitColor;
import aethereal.module.render.HitParticles;
import aethereal.module.render.HpCounter;
import aethereal.module.render.Interface_2;
import aethereal.module.render.ItemHighlight;
import aethereal.module.render.ItemPhysic;
import aethereal.module.render.MotionBlur;
import aethereal.module.render.NameTags;
import aethereal.module.render.NoFluid;
import aethereal.module.render.Pointers;
import aethereal.module.render.Predictions;
import aethereal.module.render.Removals;
import aethereal.module.render.SeeInvisibles;
import aethereal.module.render.SelfTag;
import aethereal.module.render.ShaderESP;
import aethereal.module.render.ShulkerPreview;
import aethereal.module.render.SoundESP;
import aethereal.module.render.SwingAnimation;
import aethereal.module.render.TargetESP;
import aethereal.module.render.Trails;
import aethereal.module.render.TransparentPlayers;
import aethereal.module.render.ViewModel;
import aethereal.module.render.WardenESP;
import aethereal.module.render.Zoom;
import aethereal.render.Animations;
import aethereal.setting.BindSetting;
import aethereal.setting.Setting;
import aethereal.ui.screen.AssistantScreen;
import aethereal.ui.screen.RadialScreen;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;
import lombok.Generated;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_2960;
import net.minecraft.class_7922;
import net.minecraft.class_7923;

public class ModuleProcessor extends ConfigProcessor<Module> {
   private final OpenWalls e = new OpenWalls();
   private final ScreenWalk f = new ScreenWalk();
   private final FakeLags g = new FakeLags();
   private final FreeCamera h = new FreeCamera();
   private final WardenESP i = new WardenESP();
   private final Structures j = new Structures();
   private final AutoAuth k = new AutoAuth();
   private final AutoDodge l = new AutoDodge();
   private final SoundESP m = new SoundESP();
   private final NoCrouch n = new NoCrouch();
   private final Sprint o = new Sprint();
   private final LockSlot p = new LockSlot();
   private final NoSlowDown q = new NoSlowDown();
   private final SoundReducer r = new SoundReducer();
   private final NoInteract s = new NoInteract();
   private final HitBoxes t = new HitBoxes();
   private final TapeMouse u = new TapeMouse();
   private final NoServerPack v = new NoServerPack();
   private final ItemScroller w = new ItemScroller();
   private final BoardSpoofer x = new BoardSpoofer();
   private final Communication y = new Communication();
   private final UseTracker z = new UseTracker();
   private final ShiftTAP A = new ShiftTAP();
   private final Aura B = new Aura();
   private final AutoExplosion C = new AutoExplosion();
   private final ProjectileHelper D = new ProjectileHelper();
   private final XRay E = new XRay();
   private final ElytraHelper F = new ElytraHelper();
   private final ElytraTarget G = new ElytraTarget();
   private final MaceHelper H = new MaceHelper();
   private final AntiAFK I = new AntiAFK();
   private final DeathCoords J = new DeathCoords();
   private final AutoAccept K = new AutoAccept();
   private final AutoSwap L = new AutoSwap();
   private final ThirdPerson M = new ThirdPerson();
   private final AutoTool N = new AutoTool();
   private final NoPush O = new NoPush();
   private final AutoRespawn P = new AutoRespawn();
   private final Animations Q = new Animations();
   private final SwingAnimation R = new SwingAnimation();
   private final AucReissue S = new AucReissue();
   private final SeeInvisibles T = new SeeInvisibles();
   private final EntityBox U = new EntityBox();
   private final AutoTotem V = new AutoTotem();
   private final AutoArmor W = new AutoArmor();
   private final TriggerBot X = new TriggerBot();
   private final AimAssistant Y = new AimAssistant();
   private final AntiBot Z = new AntiBot();
   private final EntityESP aa = new EntityESP();
   private final BlockESP ab = new BlockESP();
   private final NoFriendDamage ac = new NoFriendDamage();
   private final ShaderESP ad = new ShaderESP();
   private final NoServerDesync ae = new NoServerDesync();
   private final NoSlotChange af = new NoSlotChange();
   private final ItemPhysic ag = new ItemPhysic();
   private final SafeWalk ah = new SafeWalk();
   private final Removals ai = new Removals();
   private final ServerAssistant aj = new ServerAssistant();
   private final MineAssistant ak = new MineAssistant();
   private final AutoFish al = new AutoFish();
   private final NoCommands am = new NoCommands();
   private final ServerJoiner an = new ServerJoiner();
   private final ViewModel ao = new ViewModel();
   private final ClickAction ap = new ClickAction();
   private final WaterJump aq = new WaterJump();
   private final ClanUpgrader ar = new ClanUpgrader();
   private final ChatHelper as = new ChatHelper();
   private final Sounds at = new Sounds();
   private final Crosshair au = new Crosshair();
   private final AirStuck av = new AirStuck();
   private final ShulkerPreview aw = new ShulkerPreview();
   private final NoDelay ax = new NoDelay();
   private final ChinaHat ay = new ChinaHat();
   private final AppleFarmer az = new AppleFarmer();
   private final AncientFarmer aA = new AncientFarmer();
   private final AspectRatio aB = new AspectRatio();
   private final Predictions aC = new Predictions();
   private final ChestStealer aD = new ChestStealer();
   private final StreamerMode aE = new StreamerMode();
   private final Ambience aF = new Ambience();
   private final PortalBypass aG = new PortalBypass();
   private final CaptchaSolver aH = new CaptchaSolver();
   private final FastEXP aI = new FastEXP();
   private final Collector_2 aJ = new Collector_2();
   private final ItemHelper aK = new ItemHelper();
   private final Pointers aL = new Pointers();
   private final Nuker aM = new Nuker();
   private final FastLoad aN = new FastLoad();
   private final FullBright aO = new FullBright();
   private final AutoEXP aP = new AutoEXP();
   private final Fly aQ = new Fly();
   private final WallClimb aR = new WallClimb();
   private final Scaffold aS = new Scaffold();
   private final HandsShader aT = new HandsShader();
   private final AutoWarden aU = new AutoWarden();
   private final AutoEat aV = new AutoEat();
   private final WindHop aW = new WindHop();
   private final FastBreak aX = new FastBreak();
   private final FunDeliver aY = new FunDeliver();
   private final PotionThrower aZ = new PotionThrower();
   private final AutoBuy ba = new AutoBuy();
   private final AutoLeave bb = new AutoLeave();
   private final Velocity bc = new Velocity();
   private final BlockOverlay be = new BlockOverlay();
   private final Trails bf = new Trails();
   private final MotionBlur bg = new MotionBlur();
   private final Atmosphere bh = new Atmosphere();
   private final Optimization bi = new Optimization();
   private final Crafter bl = new Crafter();
   private final AutoSell bm = new AutoSell();
   private final WindJump bn = new WindJump();
   private final Buttons bo = new Buttons();
   private final Zoom bp = new Zoom();
   private final NoFluid bq = new NoFluid();
   private final HitColor br = new HitColor();
   private final FreeLook bs = new FreeLook();
   private final HitSounds bt = new HitSounds();
   private final ArmorNotifier bu = new ArmorNotifier();
   private final AHHelper bw = new AHHelper();
   private final Arrows bx = new Arrows();
   private final AntiCrash by = new AntiCrash();
   private final KTLeave bz = new KTLeave();
   private final ElytraBooster ca = new ElytraBooster();
   private final ElytraMotion cb = new ElytraMotion();
   private final CrossCart cc = new CrossCart();
   private final WindBoost cd = new WindBoost();
   private final ItemHighlight ce = new ItemHighlight();
   private final HpCounter cf = new HpCounter();
   private final HandSwap cg = new HandSwap();
   private final CrystalOptimizer ch = new CrystalOptimizer();
   private final ElytraSwap ci = new ElytraSwap();
   private final TotemTracker cj = new TotemTracker();
   private final CustomHotbar ck = new CustomHotbar();
   private final AutoCommands cl = new AutoCommands();
   private final ClickPearl cm = new ClickPearl();
   private final SelfTag cn = new SelfTag();
   private final NameTags co = new NameTags();
   private final TargetESP cp = new TargetESP();
   private final HitParticles cq = new HitParticles();
   private final AutoDuel cr = new AutoDuel();
   private final FakePlayer cs = new FakePlayer();
   private final Profiler ct = new Profiler();
   private final Emotions cu = new Emotions();
   private final AutoEnd cv = new AutoEnd();
   private final Criticals cw = new Criticals();
   private final AutoRelease cx = new AutoRelease();
   private final Speed cy = new Speed();
   private final Strafe cz = new Strafe();
   private final FastLadder cA = new FastLadder();
   private final ElytraRecast cB = new ElytraRecast();
   private final TransparentPlayers cC = new TransparentPlayers();
   private final BuildViewer cD = new BuildViewer();
   private final SchematicRecorder cE = new SchematicRecorder();
   private final PvpSafe bj = new PvpSafe();
   private final AntiDrop bk = new AntiDrop();
   private Interface_2 bd;

   @Compile
   @Override
   public void setup() {
      this.bd = new Interface_2();
      this.a(
         this.f,
         this.aA,
         this.az,
         this.bc,
         this.aY,
         this.aL,
         this.aD,
         this.Z,
         this.J,
         this.aK,
         this.S,
         this.aJ,
         this.N,
         this.aH,
         this.aT,
         this.s,
         this.av,
         this.i,
         this.aE,
         this.ao,
         this.m,
         this.Q,
         this.ag,
         this.aC,
         this.n,
         this.am,
         this.I,
         this.h,
         this.aQ,
         this.aR,
         this.aS,
         this.aI,
         this.al,
         this.aq,
         this.x,
         this.ar,
         this.g,
         this.aM,
         this.aG,
         this.aF,
         this.z,
         this.ax,
         this.aw,
         this.r,
         this.u,
         this.y,
         this.an,
         this.aj,
         this.ak,
         this.A,
         this.t,
         this.ah,
         this.V,
         this.W,
         this.as,
         this.q,
         this.P,
         this.ap,
         this.k,
         this.l,
         this.F,
         this.G,
         this.H,
         this.B,
         this.C,
         this.R,
         this.D,
         this.X,
         this.j,
         this.M,
         this.ay,
         this.L,
         this.K,
         this.o,
         this.E,
         this.ae,
         this.v,
         this.ac,
         this.af,
         this.T,
         this.ai,
         this.ab,
         this.aa,
         this.O,
         this.p,
         this.w,
         this.bd,
         this.at,
         this.au,
         this.aB,
         this.aO,
         this.e,
         this.aN,
         this.aP,
         this.aU,
         this.aV,
         this.aW,
         this.aX,
         this.aZ,
         this.ba,
         this.bb,
         this.Y,
         this.be,
         this.bf,
         this.bg,
         this.bh,
         this.bi,
         this.bj,
         this.bk,
         this.bl,
         this.bm,
         this.bn,
         this.bo,
         this.bp,
         this.bq,
         this.br,
         this.bs,
         this.bt,
         this.bu,
         this.bw,
         this.bx,
         this.by,
         this.bz,
         this.ca,
         this.cb,
         this.cc,
         this.cd,
         this.ce,
         this.cf,
         this.cg,
         this.ch,
         this.ci,
         this.cj,
         this.ck,
         this.cl,
         this.cm,
         this.cn,
         this.co,
         this.cp,
         this.cq,
         this.cr,
         this.cs,
         this.ct,
         this.cu,
         this.cv,
         this.cw,
         this.cx,
         this.cy,
         this.cz,
         this.cA,
         this.cB,
         this.cC,
         this.cD,
         this.cE,
         this.U,
         this.ad
      );
      super.setup();
   }

   @Compile
   @Override
   protected List<Module> a(String json) {
      PotionThrower potionThrower = this.aZ;
      if (json != null && !json.isBlank() && !json.trim().startsWith("[")) {
         JSONObject jSONObject = new JSONObject(json);
         if (jSONObject.m("theme")) {
            Westra.h().d().o().a(jSONObject.j("theme"));
         }

         JSONArray jSONArrayI = jSONObject.i("modules");
         if (jSONArrayI == null) {
            throw new NullPointerException();
         } else {
            for (int i = 0; i < jSONArrayI.a(); i++) {
               JSONObject jSONObjectJ = jSONArrayI.j(i);
               if (jSONObjectJ == null) {
                  throw new NullPointerException();
               }

               String strL = jSONObjectJ.l("name");
               List<Module> listE = this.e();
               if (listE == null) {
                  throw new NullPointerException();
               }

               Stream<Module> stream = listE.stream();
               Predicate<? super Module> predicate = obj -> obj.j().equalsIgnoreCase(strL);
               if (stream == null) {
                  throw new NullPointerException();
               }

               Stream<Module> streamFilter = stream.filter(predicate);
               if (streamFilter == null) {
                  throw new NullPointerException();
               }

               Optional<Module> optionalFindFirst = streamFilter.findFirst();
               Consumer<? super Module> consumer = obj -> a(jSONObjectJ, obj);
               if (optionalFindFirst == null) {
                  throw new NullPointerException();
               }

               optionalFindFirst.ifPresent(consumer);
            }

            JSONArray jSONArrayY = jSONObject.y("layouts");
            if (jSONArrayY != null) {
               Westra westra = Westra.h();
               if (westra == null) {
                  throw new NullPointerException();
               }

               Processor_2 processor_2D = westra.d();
               if (processor_2D == null) {
                  throw new NullPointerException();
               }

               CommandProcessor commandProcessorU = processor_2D.u();
               if (commandProcessorU == null) {
                  throw new NullPointerException();
               }

               LayoutCommand layoutCommandE = commandProcessorU.e();
               if (layoutCommandE == null) {
                  throw new NullPointerException();
               }

               List<LayoutCommand.a> listC = layoutCommandE.c();
               if (listC == null) {
                  throw new NullPointerException();
               }

               listC.clear();

               for (int i2 = 0; i2 < jSONArrayY.a(); i2++) {
                  JSONObject jSONObjectJ2 = jSONArrayY.j(i2);
                  if (jSONObjectJ2 == null) {
                     throw new NullPointerException();
                  }

                  class_7922 class_7922Var = class_7923.field_41178;
                  class_2960 class_2960VarMethod_60654 = class_2960.method_60654(jSONObjectJ2.l("item"));
                  if (class_7922Var == null) {
                     throw new NullPointerException();
                  }

                  Object objMethod_63535 = class_7922Var.method_63535(class_2960VarMethod_60654);
                  class_1792 class_1792Var = class_1802.field_8162;
                  if (objMethod_63535 != null && !(objMethod_63535 instanceof class_1792)) {
                     throw new ClassCastException();
                  }

                  class_1792 class_1792Var2 = (class_1792)objMethod_63535;
                  if (class_1792Var2 != class_1792Var) {
                     listC.add(new LayoutCommand.a(jSONObjectJ2.l("name"), new class_1799(class_1792Var2), jSONObjectJ2.h("slot")));
                  }
               }
            }

            JSONArray jSONArrayY2 = jSONObject.y("assistant");
            if (jSONArrayY2 != null) {
               if (potionThrower == null) {
                  throw new NullPointerException();
               }

               AssistantScreen assistantScreenQ = potionThrower.q();
               int iA = jSONArrayY2.a();
               if (assistantScreenQ == null) {
                  throw new NullPointerException();
               }

               assistantScreenQ.a(iA);

               for (int i3 = 0; i3 < jSONArrayY2.a(); i3++) {
                  Stream stream2 = Arrays.stream(AutoBuyEntry.values());
                  int i4 = i3;
                  Predicate predicate2 = obj -> ((AutoBuyEntry)obj).name().equals(jSONArrayY2.l(i4));
                  if (stream2 == null) {
                     throw new NullPointerException();
                  }

                  Stream streamFilter2 = stream2.filter(predicate2);
                  if (streamFilter2 == null) {
                     throw new NullPointerException();
                  }

                  Optional optionalFindFirst2 = streamFilter2.findFirst();
                  int i5 = i3;
                  Consumer consumer2 = obj -> assistantScreenQ.a(i5, (AutoBuyEntry)obj);
                  if (optionalFindFirst2 == null) {
                     throw new NullPointerException();
                  }

                  optionalFindFirst2.ifPresent(consumer2);
               }
            }

            return new ArrayList<>(this.e());
         }
      } else {
         return null;
      }
   }

   @Compile
   @Override
   protected String a(List<Module> data) {
      PotionThrower potionThrower = this.aZ;
      JSONArray jSONArray = new JSONArray();
      if (data == null) {
         throw new NullPointerException();
      } else {
         Iterator<Module> it = data.iterator();
         if (it == null) {
            throw new NullPointerException();
         } else {
            while (it.hasNext()) {
               Module next = it.next();
               JSONObject jSONObject = new JSONObject();
               if (next != null && !(next instanceof Module)) {
                  throw new ClassCastException();
               }

               if (next == null) {
                  throw new NullPointerException();
               }

               jSONObject.c("name", next.j());
               jSONObject.b("activated", next.m());
               jSONObject.b("bind", next.p());
               JSONObject jSONObject2 = new JSONObject();
               List<Setting<?>> listE = next.e();
               if (listE == null) {
                  throw new NullPointerException();
               }

               Iterator<Setting<?>> it2 = listE.iterator();
               if (it2 == null) {
                  throw new NullPointerException();
               }

               while (it2.hasNext()) {
                  Setting<?> next2 = it2.next();
                  if (next2 != null && !(next2 instanceof Setting)) {
                     throw new ClassCastException();
                  }

                  if (next2 == null) {
                     throw new NullPointerException();
                  }

                  if (next2.j()) {
                     jSONObject2.c(next2.i(), ConverterUtil.a(next2));
                  }
               }

               jSONObject.c("settings", jSONObject2);
               jSONArray.a(jSONObject);
            }

            JSONArray jSONArray2 = new JSONArray();
            Westra westra = Westra.h();
            if (westra == null) {
               throw new NullPointerException();
            } else {
               Processor_2 processor_2D = westra.d();
               if (processor_2D == null) {
                  throw new NullPointerException();
               } else {
                  CommandProcessor commandProcessorU = processor_2D.u();
                  if (commandProcessorU == null) {
                     throw new NullPointerException();
                  } else {
                     LayoutCommand layoutCommandE = commandProcessorU.e();
                     if (layoutCommandE == null) {
                        throw new NullPointerException();
                     } else {
                        List<LayoutCommand.a> listC = layoutCommandE.c();
                        if (listC == null) {
                           throw new NullPointerException();
                        } else {
                           Iterator<LayoutCommand.a> it3 = listC.iterator();
                           if (it3 == null) {
                              throw new NullPointerException();
                           } else {
                              while (it3.hasNext()) {
                                 LayoutCommand.a next3 = it3.next();
                                 JSONObject jSONObject3 = new JSONObject();
                                 if (next3 != null && !(next3 instanceof LayoutCommand.a)) {
                                    throw new ClassCastException();
                                 }

                                 if (next3 == null) {
                                    throw new NullPointerException();
                                 }

                                 jSONObject3.c("name", next3.a());
                                 class_7922 class_7922Var = class_7923.field_41178;
                                 class_1799 class_1799VarB = next3.b();
                                 if (class_1799VarB == null) {
                                    throw new NullPointerException();
                                 }

                                 class_1792 class_1792VarMethod_7909 = class_1799VarB.method_7909();
                                 if (class_7922Var == null) {
                                    throw new NullPointerException();
                                 }

                                 class_2960 class_2960VarMethod_10221 = class_7922Var.method_10221(class_1792VarMethod_7909);
                                 if (class_2960VarMethod_10221 == null) {
                                    throw new NullPointerException();
                                 }

                                 jSONObject3.c("item", class_2960VarMethod_10221.toString());
                                 jSONObject3.b("slot", next3.c());
                                 jSONArray2.a(jSONObject3);
                              }

                              JSONArray jSONArray3 = new JSONArray();
                              if (potionThrower == null) {
                                 throw new NullPointerException();
                              } else {
                                 AssistantScreen assistantScreenQ = potionThrower.q();
                                 if (assistantScreenQ == null) {
                                    throw new NullPointerException();
                                 } else {
                                    RadialScreen radialScreenA = assistantScreenQ.a();
                                    if (radialScreenA == null) {
                                       throw new NullPointerException();
                                    } else {
                                       for (int i = 0; i < radialScreenA.a(); i++) {
                                          Stream stream = Arrays.stream(AutoBuyEntry.values());
                                          int i2 = i;
                                          Predicate predicate = obj -> ((AutoBuyEntry)obj).a(radialScreenA.c(i2));
                                          if (stream == null) {
                                             throw new NullPointerException();
                                          }

                                          Stream streamFilter = stream.filter(predicate);
                                          if (streamFilter == null) {
                                             throw new NullPointerException();
                                          }

                                          Optional optionalFindFirst = streamFilter.findFirst();
                                          Function function = new Function() {
                                             @Override
                                             public Object apply(Object obj) {
                                                return ((Enum)obj).name();
                                             }
                                          };
                                          if (optionalFindFirst == null) {
                                             throw new NullPointerException();
                                          }

                                          Optional map = optionalFindFirst.map(function);
                                          if (map == null) {
                                             throw new NullPointerException();
                                          }

                                          jSONArray3.a(map.orElse(""));
                                       }

                                       JSONObject jSONObject4 = new JSONObject();
                                       jSONObject4.c("modules", jSONArray);
                                       jSONObject4.c("layouts", jSONArray2);
                                       jSONObject4.c("assistant", jSONArray3);
                                       jSONObject4.c("theme", Westra.h().d().o().f());
                                       return jSONObject4.a(2);
                                    }
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   @Generated
   public OpenWalls a() {
      return this.e;
   }

   @Generated
   public ScreenWalk f() {
      return this.f;
   }

   @Generated
   public FakeLags g() {
      return this.g;
   }

   @Generated
   public FreeCamera h() {
      return this.h;
   }

   @Generated
   public WardenESP i() {
      return this.i;
   }

   @Generated
   public Structures j() {
      return this.j;
   }

   @Generated
   public AutoAuth k() {
      return this.k;
   }

   @Generated
   public AutoDodge l() {
      return this.l;
   }

   @Generated
   public SoundESP m() {
      return this.m;
   }

   @Generated
   public NoCrouch n() {
      return this.n;
   }

   @Generated
   public Sprint o() {
      return this.o;
   }

   @Generated
   public LockSlot p() {
      return this.p;
   }

   @Generated
   public NoSlowDown q() {
      return this.q;
   }

   @Generated
   public SoundReducer r() {
      return this.r;
   }

   @Generated
   public NoInteract s() {
      return this.s;
   }

   @Generated
   public HitBoxes t() {
      return this.t;
   }

   @Generated
   public TapeMouse u() {
      return this.u;
   }

   @Generated
   public NoServerPack v() {
      return this.v;
   }

   @Generated
   public ItemScroller w() {
      return this.w;
   }

   @Generated
   public BoardSpoofer x() {
      return this.x;
   }

   @Generated
   public Communication y() {
      return this.y;
   }

   @Generated
   public UseTracker z() {
      return this.z;
   }

   @Generated
   public ShiftTAP A() {
      return this.A;
   }

   @Generated
   public Aura B() {
      return this.B;
   }

   @Generated
   public AutoExplosion C() {
      return this.C;
   }

   @Generated
   public ProjectileHelper D() {
      return this.D;
   }

   @Generated
   public XRay E() {
      return this.E;
   }

   @Generated
   public ElytraHelper F() {
      return this.F;
   }

   @Generated
   public ElytraTarget G() {
      return this.G;
   }

   @Generated
   public MaceHelper H() {
      return this.H;
   }

   @Generated
   public AntiAFK I() {
      return this.I;
   }

   @Generated
   public DeathCoords J() {
      return this.J;
   }

   @Generated
   public AutoAccept K() {
      return this.K;
   }

   @Generated
   public AutoSwap L() {
      return this.L;
   }

   @Generated
   public ThirdPerson M() {
      return this.M;
   }

   @Generated
   public AutoTool N() {
      return this.N;
   }

   @Generated
   public NoPush O() {
      return this.O;
   }

   @Generated
   public AutoRespawn P() {
      return this.P;
   }

   @Generated
   public Animations Q() {
      return this.Q;
   }

   @Generated
   public SwingAnimation R() {
      return this.R;
   }

   @Generated
   public AucReissue S() {
      return this.S;
   }

   @Generated
   public SeeInvisibles T() {
      return this.T;
   }

   @Generated
   public EntityBox U() {
      return this.U;
   }

   @Generated
   public AutoTotem V() {
      return this.V;
   }

   @Generated
   public AutoArmor W() {
      return this.W;
   }

   @Generated
   public TriggerBot X() {
      return this.X;
   }

   @Generated
   public AimAssistant Y() {
      return this.Y;
   }

   @Generated
   public AntiBot Z() {
      return this.Z;
   }

   @Generated
   public EntityESP aa() {
      return this.aa;
   }

   @Generated
   public BlockESP ab() {
      return this.ab;
   }

   @Generated
   public NoFriendDamage ac() {
      return this.ac;
   }

   @Generated
   public ShaderESP ad() {
      return this.ad;
   }

   @Generated
   public NoServerDesync ae() {
      return this.ae;
   }

   @Generated
   public NoSlotChange af() {
      return this.af;
   }

   @Generated
   public ItemPhysic ag() {
      return this.ag;
   }

   @Generated
   public SafeWalk ah() {
      return this.ah;
   }

   @Generated
   public Removals ai() {
      return this.ai;
   }

   @Generated
   public ServerAssistant aj() {
      return this.aj;
   }

   @Generated
   public MineAssistant ak() {
      return this.ak;
   }

   @Generated
   public AutoFish al() {
      return this.al;
   }

   @Generated
   public NoCommands am() {
      return this.am;
   }

   @Generated
   public ServerJoiner an() {
      return this.an;
   }

   @Generated
   public ViewModel ao() {
      return this.ao;
   }

   @Generated
   public ClickAction ap() {
      return this.ap;
   }

   @Generated
   public WaterJump aq() {
      return this.aq;
   }

   @Generated
   public ClanUpgrader ar() {
      return this.ar;
   }

   @Generated
   public ChatHelper as() {
      return this.as;
   }

   @Generated
   public Sounds at() {
      return this.at;
   }

   @Generated
   public Crosshair au() {
      return this.au;
   }

   @Generated
   public AirStuck av() {
      return this.av;
   }

   @Generated
   public ShulkerPreview aw() {
      return this.aw;
   }

   @Generated
   public NoDelay ax() {
      return this.ax;
   }

   @Generated
   public ChinaHat ay() {
      return this.ay;
   }

   @Generated
   public AppleFarmer az() {
      return this.az;
   }

   @Generated
   public AncientFarmer aA() {
      return this.aA;
   }

   @Generated
   public AspectRatio aB() {
      return this.aB;
   }

   @Generated
   public Predictions aC() {
      return this.aC;
   }

   @Generated
   public ChestStealer aD() {
      return this.aD;
   }

   @Generated
   public StreamerMode aE() {
      return this.aE;
   }

   @Generated
   public Ambience aF() {
      return this.aF;
   }

   @Generated
   public PortalBypass aG() {
      return this.aG;
   }

   @Generated
   public CaptchaSolver aH() {
      return this.aH;
   }

   @Generated
   public FastEXP aI() {
      return this.aI;
   }

   @Generated
   public Collector_2 aJ() {
      return this.aJ;
   }

   @Generated
   public ItemHelper aK() {
      return this.aK;
   }

   @Generated
   public Pointers aL() {
      return this.aL;
   }

   @Generated
   public Nuker aM() {
      return this.aM;
   }

   @Generated
   public FastLoad aN() {
      return this.aN;
   }

   @Generated
   public FullBright aO() {
      return this.aO;
   }

   @Generated
   public AutoEXP aP() {
      return this.aP;
   }

   @Generated
   public Fly aQ() {
      return this.aQ;
   }

   @Generated
   public WallClimb aR() {
      return this.aR;
   }

   @Generated
   public Scaffold aS() {
      return this.aS;
   }

   @Generated
   public HandsShader aT() {
      return this.aT;
   }

   @Generated
   public AutoWarden aU() {
      return this.aU;
   }

   @Generated
   public AutoEat aV() {
      return this.aV;
   }

   @Generated
   public WindHop aW() {
      return this.aW;
   }

   @Generated
   public FastBreak aX() {
      return this.aX;
   }

   @Generated
   public FunDeliver aY() {
      return this.aY;
   }

   @Generated
   public PotionThrower aZ() {
      return this.aZ;
   }

   @Generated
   public AutoBuy ba() {
      return this.ba;
   }

   @Generated
   public AutoLeave bb() {
      return this.bb;
   }

   @Generated
   public Velocity bc() {
      return this.bc;
   }

   @Generated
   public AntiDrop bk() {
      return this.bk;
   }

   @Generated
   public PvpSafe bj() {
      return this.bj;
   }

   @Generated
   public Zoom bp() {
      return this.bp;
   }

   @Generated
   public FreeLook bs() {
      return this.bs;
   }

   @Generated
   public HitSounds bt() {
      return this.bt;
   }

   @Generated
   public ArmorNotifier bu() {
      return this.bu;
   }

   @Generated
   public AHHelper bw() {
      return this.bw;
   }

   @Generated
   public Arrows bx() {
      return this.bx;
   }

   @Generated
   public AntiCrash by() {
      return this.by;
   }

   @Generated
   public KTLeave bz() {
      return this.bz;
   }

   @Generated
   public ElytraBooster ca() {
      return this.ca;
   }

   @Generated
   public ElytraMotion cb() {
      return this.cb;
   }

   @Generated
   public Emotions cu() {
      return this.cu;
   }

   public NameTags co() {
      return this.co;
   }

   public SelfTag cn() {
      return this.cn;
   }

   public CustomHotbar ck() {
      return this.ck;
   }

   public ItemHighlight ce() {
      return this.ce;
   }

   public CrossCart cc() {
      return this.cc;
   }

   @Generated
   public NoFluid bq() {
      return this.bq;
   }

   @Generated
   public HitColor br() {
      return this.br;
   }

   @Generated
   public Buttons bo() {
      return this.bo;
   }

   @Generated
   public WindJump bn() {
      return this.bn;
   }

   @Generated
   public AutoSell bm() {
      return this.bm;
   }

   @Generated
   public Crafter bl() {
      return this.bl;
   }

   @Generated
   public Optimization bi() {
      return this.bi;
   }

   @Generated
   public Atmosphere bh() {
      return this.bh;
   }

   @Generated
   public MotionBlur bg() {
      return this.bg;
   }

   @Generated
   public Trails bf() {
      return this.bf;
   }

   @Generated
   public BlockOverlay be() {
      return this.be;
   }

   @Generated
   public Interface_2 bd() {
      return this.bd;
   }

   @Override
   public void unSetup() {
      super.unSetup();
   }

   @Override
   public File d() {
      return this.b;
   }

   @Override
   protected String b() {
      return "default.westra";
   }

   @EventTarget
   public void a(KeyEvent event) {
      int action = event.d();
      int key = event.b();

      for (Module module : this.e()) {
         if (module.p() != -1 && module.p() == key && action == 1) {
            module.a();
         }

         if (module.m()) {
            for (Setting<?> setting : module.e()) {
               if (setting instanceof BindSetting bind && bind.e().get() && bind.c() != -1 && bind.c() == key) {
                  if (action == 1 && bind.k() != null) {
                     bind.k().execute();
                  } else if (action == 0 && bind.m() == 0 && bind.l() != null) {
                     bind.l().execute();
                  }
               }
            }
         }
      }
   }

   public static void a(JSONObject obj, Module module) {
      module.a(obj.a("activated", false));
      module.a(obj.a("bind", -1));
      if (obj.m("settings")) {
         JSONObject settingsObj = obj.j("settings");

         for (Setting<?> setting : module.e()) {
            if (settingsObj.m(setting.i())) {
               ConverterUtil.a(setting, settingsObj.a(setting.i()));
            }
         }
      }
   }

   public void b(String rawName) {
      try {
         String configName = e(rawName);
         Files.writeString(new File(this.d(), configName + ".westra").toPath(), this.a(this.d));
      } catch (IOException var3) {
         throw new RuntimeException(var3);
      }
   }

   private static String e(String name) {
      if (name.endsWith(".westra")) {
         return name.substring(0, name.length() - ".westra".length());
      } else {
         return name.endsWith(".json") ? name.substring(0, name.length() - 5) : name;
      }
   }

   public boolean c(String raw) {
      try {
         String str = e(raw);
         File file = new File(this.d(), str + ".westra");
         if (!file.exists()) {
            file = new File(this.d(), str + ".json");
         }

         if (!file.exists()) {
            return false;
         } else {
            List<Module> listA = this.a(Files.readString(file.toPath()));
            if (listA != null) {
               this.d.clear();
               this.d.addAll(listA);
               return true;
            } else {
               return true;
            }
         }
      } catch (Exception var5) {
         return false;
      }
   }

   public boolean d(String rawName) {
      String configName = e(rawName);
      File configFile = new File(this.d(), configName + ".westra");
      if (!configFile.exists()) {
         configFile = new File(this.d(), configName + ".json");
      }

      return configFile.exists() && !configName.equals(this.b()) ? configFile.delete() : false;
   }

   private void a(Module... modules) {
      Collections.addAll(this.d, modules);
   }

   public Criticals cw() {
      return this.cw;
   }

   public AutoRelease cx() {
      return this.cx;
   }

   public Speed cy() {
      return this.cy;
   }

   public Strafe cz() {
      return this.cz;
   }

   public FastLadder cA() {
      return this.cA;
   }

   public ElytraRecast cB() {
      return this.cB;
   }

   public TransparentPlayers cC() {
      return this.cC;
   }

   public BuildViewer cD() {
      return this.cD;
   }

   public SchematicRecorder cE() {
      return this.cE;
   }

   static {
      NativeMethodLookup.lookup(ModuleProcessor.class, 33);
   }
}
