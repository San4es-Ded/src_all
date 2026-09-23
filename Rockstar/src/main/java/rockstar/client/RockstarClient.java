package rockstar.client;



















import rockstar.client.rotation.*;
import rockstar.client.render.*;
import rockstar.client.notification.*;
import rockstar.client.module.*;
import rockstar.client.i18n.*;
import rockstar.client.esp.*;
import rockstar.client.asset.*;
import rockstar.client.internal.ui.*;
import rockstar.client.internal.script.*;
import rockstar.client.internal.rotation.*;
import rockstar.client.internal.render.*;
import rockstar.client.internal.network.*;
import rockstar.client.internal.game.*;
import rockstar.client.internal.framework.*;
import rockstar.client.internal.core.*;
import rockstar.client.internal.config.*;
import rockstar.client.internal.command.*;
import rockstar.client.internal.auth.*;
import globals.client.RocknetListener;
import globals.client.api.RockNetClient;
import globals.client.auth.SessionManager;
import globals.client.snowball.SnowballManager;
import globals.client.ui.RocknetMenu;
import lombok.Generated;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.funtimeevents.api.FunTimeEventsAPI;
import net.minecraft.entity.Entity;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum RockstarClient implements MinecraftClientAccess {
   internalField0240;

   public static final String internalField0248 = "NoName";
   public static final String internalField0247 = "2.1";
   public static final String internalField1077 = "rockstar";
   public static final String internalField1076 = "https://ftapi.rockstar.pub/v1/";
   public static final String internalField1079 = "rockstar.pub";
   public static final int internalField0227 = 443;
   public static final boolean internalField0277 = true;
   public static final String internalField1078 = "https://rockstar.pub";
   public static final Logger internalField0572 = LoggerFactory.getLogger(internalField1077);
   public static Entity internalField0410;
   private EventBus internalField0159;
   private ThemeModeState internalField0396;
   private ModuleManager internalField0404;
   private CommandRegistry internalField0421;
   private FriendListManager internalField0163;
   private DiscordRichPresenceService internalField0156;
   private RotationManager internalField0120;
   private NewtonCoreManager internalField0774;
   private TargetSelectionManager internalField0392;
   private MediaSessionProvider internalField0516;
   private ConfigManager internalField0161;
   private NotificationManager internalField0468;
   private LocalConfigStore internalField0044;
   private ChatMacroManager internalField0390;
   private SwingAnimationManager internalField0730;
   private ServerTpsTracker internalField0580;
   private HudManager internalField0938;
   private ClipboardWaypointHandler internalField0399;
   private ChatWaypointHandler internalField0398;
   private WaypointContextMenuHandler internalField0562;
   private KeyComboAnimationListener internalField0433;
   private WaypointStore internalField0561;
   private SwingPresetManager internalField0753;
   private StaffListStore internalField0388;
   private boolean internalField0276;
   private RockNetClient internalField0235;
   private PythonScriptManager internalField0434;
   private ScriptSyncService internalField0444;
   private ClientDataSyncService internalField0160;
   private AutoFarmWebBridge internalField0712;
   private AbstractMenuScreen internalField0375;
   private RocknetMenu internalField0660;
   private AuctionCommandHandler internalField0367;
   boolean internalField1099;

   public void initialize() {
      internalField0572.info("Initializing {}...", "NoName");
      SessionManager.bootstrapEarly("https://rockstar.pub/api/v1");
      this.internalField0516 = new MediaSessionProvider();
      this.internalField0561 = new WaypointStore();
      this.internalField0434 = new PythonScriptManager();
      this.internalField0159 = new EventBus();
      this.internalField0163 = new FriendListManager();
      this.internalField0388 = new StaffListStore();
      this.internalField0396 = new ThemeModeState();
      this.internalField0156 = new DiscordRichPresenceService();
      this.internalField0120 = new RotationManager(new RotationEventHandler());
      this.internalField0774 = new NewtonCoreManager();
      this.internalField0392 = new TargetSelectionManager();
      this.internalField0161 = new ConfigManager();
      this.internalField0404 = new ModuleManager(new ModuleTickDispatcher(), new HudRenderStub());
      this.internalField0938 = new HudManager();
      this.internalField0580 = new ServerTpsTracker();
      this.internalField0468 = new NotificationManager();
      this.internalField0161.internalMethod05330();
      this.internalField0404.registerModules();
      this.internalField0404.enableDefaultModules();
      this.internalField0235 = RockNetClient.init("rockstar.pub", 443, true);
      this.internalField0235.setListener(new RocknetListener());

      try {
         FunTimeEventsAPI.builder().userAgent("NoName").baseUrl("https://ftapi.rockstar.pub/v1/").build();
      } catch (Throwable localValue2) {
         internalField0572.warn("FunTimeEvents SDK \u043d\u0435 \u043f\u043e\u0434\u043d\u044f\u043b\u0441\u044f: {}", localValue2.toString());
      }

      PythonRuntimeInstaller.internalMethod01801(this.internalField0235.getHttpBase());
      AssetPackInstaller.internalMethod02290(this.internalField0235.getHttpBase());
      SnowballManager.getInstance().register();
      EspManager.internalMethod06726().internalMethod03144();
      this.internalField0044 = new LocalConfigStore();
      this.internalField0044.internalMethod03462();
      this.internalField0444 = new ScriptSyncService();
      this.internalField0160 = new ClientDataSyncService();
      this.internalField0712 = new AutoFarmWebBridge();
      this.internalField0421 = new CommandRegistry();
      this.internalField0421.internalMethod03368();
      this.internalField0390 = new ChatMacroManager();
      this.internalField0730 = new SwingAnimationManager();
      this.internalField0753 = new SwingPresetManager();
      this.internalField0753.internalMethod05545();
      this.internalField0161.internalMethod05336();
      this.internalField0235.connect();
      SessionManager.bootstrapAsync(this.internalField0235);
      ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
         public Identifier getFabricId() {
            return RockstarClient.id("after_shader_load");
         }

         public void reload(ResourceManager localValue1) {
            if (!RockstarClient.this.internalField1099) {
               Fonts.internalMethod05887();
               IconFontRegistry.internalMethod02914();
               ShaderProgramBase.internalMethod04681();
            }
         }
      });
      RenderPipeline.internalMethod01907();
      ShaderProgramBase.internalMethod02836(true);
      LanguageManager.internalMethod05353();
      this.internalField0367 = new AuctionCommandHandler();
      this.internalField0398 = new ChatWaypointHandler();
      this.internalField0399 = new ClipboardWaypointHandler();
      this.internalField0562 = new WaypointContextMenuHandler();
      this.internalField0433 = new KeyComboAnimationListener();
      String localValue1 = System.getProperty("user.name");
      this.internalField0276 = !System.getProperty("os.name").toLowerCase().contains("windows");
      if (!this.internalField0276 && !localValue1.equals("sheluvparis")) {
         this.internalField0156.internalMethod00959();
      }

      ConfigFileDropHandler.internalMethod05313();
      TitleBarThemeHelper.internalMethod01735();
      new ItemCooldownHolder();
      new SessionReporter();
      McpHttpServer.internalMethod07531().internalMethod01544();
      internalField0572.info("{} initialized", "NoName");
   }

   public void shutdown() {
      internalField0572.info("Shutting down...");
      if (!this.internalMethod06896()) {
         this.internalField0044.internalMethod07805();
         if (this.internalField0160 != null) {
            this.internalField0160.internalMethod05805();
         }

         this.internalField0753.internalMethod01444().internalMethod03767();
      }

      this.internalField0235.close();
      if (this.internalField0444 != null) {
         this.internalField0444.internalMethod08550();
      }

      if (this.internalField0160 != null) {
         this.internalField0160.internalMethod08886();
      }

      this.internalField0161.internalMethod08923();
      this.internalMethod00631(false);
   }

   public static RockstarClient getInstance() {
      return internalField0240;
   }

   public static Identifier id(String localValue0) {
      return Identifier.of(internalField1077, localValue0);
   }

   @Generated
   public EventBus internalMethod03317() {
      return this.internalField0159;
   }

   @Generated
   public ThemeModeState internalMethod04467() {
      return this.internalField0396;
   }

   @Generated
   public ModuleManager getModuleManager() {
      return this.internalField0404;
   }

   @Generated
   public CommandRegistry internalMethod05348() {
      return this.internalField0421;
   }

   @Generated
   public FriendListManager internalMethod03375() {
      return this.internalField0163;
   }

   @Generated
   public DiscordRichPresenceService internalMethod03315() {
      return this.internalField0156;
   }

   @Generated
   public RotationManager internalMethod02368() {
      return this.internalField0120;
   }

   @Generated
   public NewtonCoreManager internalMethod07318() {
      return this.internalField0774;
   }

   @Generated
   public TargetSelectionManager internalMethod04463() {
      return this.internalField0392;
   }

   @Generated
   public MediaSessionProvider internalMethod05636() {
      return this.internalField0516;
   }

   @Generated
   public ConfigManager internalMethod03371() {
      return this.internalField0161;
   }

   @Generated
   public NotificationManager internalMethod02503() {
      return this.internalField0468;
   }

   @Generated
   public LocalConfigStore internalMethod02152() {
      return this.internalField0044;
   }

   @Generated
   public ChatMacroManager internalMethod05155() {
      return this.internalField0390;
   }

   @Generated
   public SwingAnimationManager internalMethod00061() {
      return this.internalField0730;
   }

   @Generated
   public ServerTpsTracker internalMethod06191() {
      return this.internalField0580;
   }

   @Generated
   public HudManager internalMethod01271() {
      return this.internalField0938;
   }

   @Generated
   public ClipboardWaypointHandler internalMethod04469() {
      return this.internalField0399;
   }

   @Generated
   public ChatWaypointHandler internalMethod04468() {
      return this.internalField0398;
   }

   @Generated
   public WaypointContextMenuHandler internalMethod06122() {
      return this.internalField0562;
   }

   @Generated
   public KeyComboAnimationListener internalMethod04978() {
      return this.internalField0433;
   }

   @Generated
   public WaypointStore internalMethod06121() {
      return this.internalField0561;
   }

   @Generated
   public SwingPresetManager internalMethod01001() {
      return this.internalField0753;
   }

   @Generated
   public StaffListStore internalMethod04407() {
      return this.internalField0388;
   }

   @Generated
   public boolean internalMethod06893() {
      return this.internalField0276;
   }

   @Generated
   public RockNetClient internalMethod06050() {
      return this.internalField0235;
   }

   @Generated
   public PythonScriptManager internalMethod04979() {
      return this.internalField0434;
   }

   @Generated
   public ScriptSyncService internalMethod05030() {
      return this.internalField0444;
   }

   @Generated
   public ClientDataSyncService internalMethod03318() {
      return this.internalField0160;
   }

   @Generated
   public AutoFarmWebBridge internalMethod04226() {
      return this.internalField0712;
   }

   @Generated
   public AbstractMenuScreen internalMethod04334() {
      return this.internalField0375;
   }

   @Generated
   public RocknetMenu internalMethod01773() {
      return this.internalField0660;
   }

   @Generated
   public AuctionCommandHandler internalMethod04298() {
      return this.internalField0367;
   }

   @Generated
   public boolean internalMethod06896() {
      return this.internalField1099;
   }

   @Generated
   public void internalMethod01597(RockNetClient localValue1) {
      this.internalField0235 = localValue1;
   }

   @Generated
   public void internalMethod02331(AbstractMenuScreen localValue1) {
      this.internalField0375 = localValue1;
   }

   @Generated
   public void internalMethod06176(RocknetMenu localValue1) {
      this.internalField0660 = localValue1;
   }

   @Generated
   public void internalMethod00631(boolean localValue1) {
      this.internalField1099 = localValue1;
   }
}
