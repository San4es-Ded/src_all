package rockstar.modules.movement;









import rockstar.client.util.*;
import rockstar.client.setting.*;
import rockstar.client.server.*;
import rockstar.client.rotation.*;
import rockstar.client.module.*;
import rockstar.client.inventory.*;
import rockstar.client.event.*;
import rockstar.client.internal.game.*;
import rockstar.client.*;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import pyrock.events.network.ReceivePacketEvent;
import pyrock.events.network.SendPacketEvent;
import pyrock.events.player.ClientPlayerTickEvent;
import pyrock.events.player.EventMotion;
import pyrock.events.player.EventOnTravelPost;
import pyrock.events.player.InputEvent;
import rockstar.modules.player.GuiMoveModule;
import rockstar.client.setting.SettingOwner;
import rockstar.client.setting.ModeSetting;
import rockstar.client.setting.SliderSetting;
import rockstar.client.event.EventListener;
import rockstar.client.module.ModuleCategory;
import rockstar.client.RockstarClient;
import rockstar.client.module.ModuleInfo;
import rockstar.client.util.CustomItemUtils;
import rockstar.client.util.ClientMessages;
import rockstar.client.server.KnownServer;
import rockstar.client.server.ServerUtils;
import rockstar.client.MinecraftClientAccess;
import rockstar.client.util.EnchantmentUtils;
import rockstar.client.inventory.InventoryUtils;
import rockstar.client.inventory.InventorySlot;
import rockstar.client.inventory.SlotCollection;
import rockstar.client.inventory.InventorySlots;
import rockstar.client.inventory.ArmorSlot;
import rockstar.client.rotation.RotationBehavior;
import rockstar.client.rotation.Rotation;
import rockstar.client.rotation.RotationPriority;
import rockstar.client.module.Module;

@ModuleInfo(name="Flight", category=ModuleCategory.MOVEMENT)
public class FlightModule
extends Module {
    private ModeSetting internalField0668;
    private ModeSetting.InternalType0088 internalField0237;
    private ModeSetting.InternalType0088 internalField0238;
    private ModeSetting.InternalType0088 internalField1066;
    private SliderSetting internalField0383;
    private boolean internalField0277;
    private boolean internalField0276;
    private boolean internalField1099;
    private float internalField0205;
    private int internalField0227;
    private boolean internalField1100;
    private boolean internalField1102;
    private boolean internalField1101;
    private final InternalType0241 internalField0014 = new InternalType0241();
    private final EventListener<EventMotion> internalField0157 = eventMotion -> {
        if (this.internalField0238.isSelected()) {
            RockstarClient.getInstance().internalMethod02368().internalMethod00418(new Rotation(FlightModule.internalField0149.player.getYaw(), 0.0f), RotationBehavior.internalField0114, 180.0f, 180.0f, 180.0f, RotationPriority.internalField1009);
            ClientPlayerEntity clientPlayerEntity = FlightModule.internalField0149.player;
            if (clientPlayerEntity != null && clientPlayerEntity.isAlive() && clientPlayerEntity.isGliding()) {
                clientPlayerEntity.setVelocity(clientPlayerEntity.getVelocity().x, clientPlayerEntity.getVelocity().y + 0.0305, clientPlayerEntity.getVelocity().z);
            }
        }
    };
    private final EventListener<InputEvent> internalField0158 = inputEvent -> {
        if (this.internalField0238.isSelected()) {
            if (InventoryUtils.internalMethod06826().internalMethod00210() != Items.ELYTRA) {
                return;
            }
            if (FlightModule.internalField0149.player.isInFluid()) {
                return;
            }
            inputEvent.setJump(FlightModule.internalField0149.player.age % 2 == 0);
        }
    };
    private final EventListener<ReceivePacketEvent> internalField1028 = receivePacketEvent -> {
        if (!this.internalField1066.isSelected()) {
            return;
        }
        if (receivePacketEvent.getPacket() instanceof PlayerPositionLookS2CPacket) {
            this.internalField0227 = 2;
            this.internalField1100 = true;
        }
    };
    private final EventListener<SendPacketEvent> internalField1029 = sendPacketEvent -> {
        if (!this.internalField1066.isSelected() || this.internalField1102) {
            return;
        }
        if (!(sendPacketEvent.getPacket() instanceof PlayerMoveC2SPacket)) {
            return;
        }
        if (FlightModule.internalField0149.player != null && FlightModule.internalField0149.player.isGliding() && this.internalField0227 == 0 && !this.internalField1100) {
            this.internalField1102 = true;
            try {
                FlightModule.internalField0149.player.networkHandler.sendPacket((Packet)new PlayerMoveC2SPacket.OnGroundOnly(true, true));
            }
            finally {
                this.internalField1102 = false;
            }
            sendPacketEvent.cancel();
        }
        this.internalField1100 = false;
    };
    private final EventListener<ClientPlayerTickEvent> internalField1030 = clientPlayerTickEvent -> {
        if (this.internalField1066.isSelected() && this.internalField0227 > 0) {
            --this.internalField0227;
        }
    };
    private final EventListener<EventOnTravelPost> internalField1027 = eventOnTravelPost -> {
        double d;
        if (!this.internalField1066.isSelected() || FlightModule.internalField0149.player == null) {
            return;
        }
        Vec3d vec3d = FlightModule.internalField0149.player.getVelocity();
        Vec3d vec3d2 = FlightModule.internalField0149.player.getRotationVector();
        float f = FlightModule.internalField0149.player.getPitch() * ((float)Math.PI / 180);
        double d2 = Math.sqrt(vec3d2.x * vec3d2.x + vec3d2.z * vec3d2.z);
        double d3 = vec3d.horizontalLength();
        boolean bl = FlightModule.internalField0149.player.getVelocity().y <= 0.0;
        double d4 = bl && FlightModule.internalField0149.player.hasStatusEffect(StatusEffects.SLOW_FALLING) ? Math.min(FlightModule.internalField0149.player.getFinalGravity(), 0.01) : FlightModule.internalField0149.player.getFinalGravity();
        double d5 = MathHelper.square((double)Math.cos(f));
        vec3d = vec3d.add(0.0, d4 * (-1.0 + d5 * 0.75), 0.0);
        if (vec3d.y < 0.0 && d2 > 0.0) {
            d = vec3d.y * -0.1 * d5;
            vec3d = vec3d.add(vec3d2.x * d / d2, d, vec3d2.z * d / d2);
        }
        if (f < 0.0f && d2 > 0.0) {
            d = d3 * (double)(-MathHelper.sin((float)f)) * (double)0.04f;
            vec3d = vec3d.add(-vec3d2.x * d / d2, d * 3.2, -vec3d2.z * d / d2);
        }
        if (d2 > 0.0) {
            vec3d = vec3d.add((vec3d2.x / d2 * d3 - vec3d.x) * 0.1, 0.0, (vec3d2.z / d2 * d3 - vec3d.z) * 0.1);
        }
        d = Math.toRadians(FlightModule.internalField0149.player.getYaw());
        double d6 = -Math.sin(d);
        double d7 = Math.cos(d);
        if (this.internalField0227 >= 1) {
            double d8 = 0.09f;
            eventOnTravelPost.setOldVelocity(vec3d.multiply((double)0.99f, (double)0.98f, (double)0.99f).add(d6 * d8, (double)0.03f, d7 * d8));
        } else {
            eventOnTravelPost.setOldVelocity(vec3d.multiply((double)0.3f, (double)0.3f, (double)0.3f));
        }
    };

    public FlightModule() {
        this.internalMethod09617();
    }

    private void internalMethod09617() {
        this.internalField0668 = new ModeSetting(this, "modules.settings.flight.mode");
        this.internalField0237 = new ModeSetting.InternalType0088(this.internalField0668, "modules.settings.flight.vanilla");
        this.internalField0238 = new ModeSetting.InternalType0088(this.internalField0668, "modules.settings.flight.elytra_y");
        this.internalField1066 = new ModeSetting.InternalType0088(this.internalField0668, "modules.settings.flight.elytra_exploit");
        this.internalField0383 = new SliderSetting((SettingOwner)this, "modules.settings.flight.speed", () -> !this.internalField0237.isSelected()).internalMethod08074(1.0f).internalMethod05900(0.1f).internalMethod02732(10.0f).internalMethod08673(0.1f);
    }

    @Override
    public final void onEnable() {
        this.internalField1101 = false;
        this.internalMethod09785();
        if (this.internalField0238.isSelected() && FlightModule.internalField0149.player != null) {
            if (!FlightModule.internalField0149.player.isOnGround()) {
                this.toggle();
                ClientMessages.internalMethod01809(Text.of((String)"\u041d\u0435\u043e\u0431\u0445\u043e\u0434\u0438\u043c\u043e \u0432\u043a\u043b\u044e\u0447\u0430\u0442\u044c \u043d\u0430 \u0437\u0435\u043c\u043b\u0435"));
                return;
            }
            if (FlightModule.internalField0149.player.getVelocity().length() > (double)0.1f) {
                this.toggle();
                ClientMessages.internalMethod01809(Text.of((String)"\u041d\u0435\u043e\u0431\u0445\u043e\u0434\u0438\u043c\u043e \u0441\u0442\u043e\u044f\u0442\u044c"));
                return;
            }
            this.internalMethod09618();
        }
        super.onEnable();
    }

    @Override
    public final void internalMethod08229() {
        if (FlightModule.internalField0149.player == null || FlightModule.internalField0149.world == null) {
            return;
        }
        if (this.internalField0237.isSelected()) {
            this.internalMethod00866(FlightModule.internalField0149.player);
            return;
        }
        this.internalMethod09776();
    }

    @Override
    public final void onDisable() {
        this.internalMethod09776();
        this.internalMethod09785();
        if (this.internalField1101 && FlightModule.internalField0149.player != null) {
            this.internalMethod09775();
        }
        this.internalField1101 = false;
        super.onDisable();
    }

    private void internalMethod09618() {
        ArmorSlot typedValue230 = InventoryUtils.internalMethod06826();
        if (typedValue230.internalMethod00210() == Items.ELYTRA) {
            return;
        }
        SlotCollection<InventorySlot> typedValue228 = InventorySlots.internalMethod02872().internalMethod07591(InventorySlots.internalMethod03558()).internalMethod07591(InventorySlots.internalMethod07766());
        InventorySlot typedValue222 = typedValue228.internalMethod03297(itemStack -> itemStack.getItem() == Items.ELYTRA && !itemStack.willBreakNextUse());
        if (typedValue222 == null) {
            return;
        }
        this.internalField1101 = true;
        this.internalField0014.internalMethod02785(typedValue222, typedValue230);
    }

    private void internalMethod09775() {
        ArmorSlot typedValue230 = InventoryUtils.internalMethod06826();
        if (typedValue230.internalMethod00210() != Items.ELYTRA) {
            return;
        }
        SlotCollection<InventorySlot> typedValue228 = InventorySlots.internalMethod02872().internalMethod07591(InventorySlots.internalMethod03558()).internalMethod07591(InventorySlots.internalMethod07766());
        InventorySlot typedValue222 = FlightModule.internalMethod04915(typedValue228);
        if (typedValue222 == null) {
            return;
        }
        this.internalField0014.internalMethod02785(typedValue222, typedValue230);
    }

    private static InventorySlot internalMethod04915(SlotCollection<InventorySlot> typedValue228) {
        InventorySlot typedValue222 = null;
        int n = Integer.MIN_VALUE;
        for (InventorySlot typedValue223 : typedValue228.internalMethod02638()) {
            int n2;
            ItemStack itemStack = typedValue223.internalMethod03427();
            if (LegacyItemTypes.getArmorSlot(itemStack) != net.minecraft.entity.EquipmentSlot.CHEST || (n2 = FlightModule.internalMethod03731(itemStack)) <= n) continue;
            n = n2;
            typedValue222 = typedValue223;
        }
        return typedValue222;
    }

    private static int internalMethod03731(ItemStack itemStack) {
        CustomItemUtils.InternalType0254 nestedValue2032 = CustomItemUtils.internalMethod03238(itemStack);
        if (nestedValue2032 != null && "SunHelmet".equals(nestedValue2032.internalMethod01319())) {
            return Integer.MAX_VALUE;
        }
        int n = LegacyItemTypes.getDefense(itemStack);
        int n2 = LegacyItemTypes.getToughness(itemStack);
        int n3 = EnchantmentUtils.internalMethod03526(itemStack, (RegistryKey<Enchantment>)Enchantments.PROTECTION);
        return n * 5 + n3 * 3 + n2;
    }

    private void internalMethod00866(ClientPlayerEntity clientPlayerEntity) {
        PlayerAbilities playerAbilities = clientPlayerEntity.getAbilities();
        if (!this.internalField0277) {
            this.internalField0276 = playerAbilities.allowFlying;
            this.internalField1099 = playerAbilities.flying;
            this.internalField0205 = playerAbilities.getFlySpeed();
            this.internalField0277 = true;
        }
        if (!playerAbilities.allowFlying) {
            playerAbilities.allowFlying = true;
        }
        if (!playerAbilities.flying) {
            playerAbilities.flying = true;
        }
        float f = Math.clamp(0.05f * this.internalField0383.internalMethod08576(), 0.0f, 1.0f);
        if (Math.abs(playerAbilities.getFlySpeed() - f) > 1.0E-4f) {
            playerAbilities.setFlySpeed(f);
        }
    }

    private void internalMethod09776() {
        if (!this.internalField0277) {
            return;
        }
        this.internalField0277 = false;
        if (FlightModule.internalField0149.player == null) {
            return;
        }
        PlayerAbilities playerAbilities = FlightModule.internalField0149.player.getAbilities();
        if (!FlightModule.internalField0149.player.isCreative() && !FlightModule.internalField0149.player.isSpectator()) {
            playerAbilities.allowFlying = this.internalField0276;
            playerAbilities.flying = this.internalField1099;
        }
        playerAbilities.setFlySpeed(this.internalField0205);
    }

    private void internalMethod09785() {
        this.internalField0227 = 0;
        this.internalField1100 = false;
        this.internalField1102 = false;
    }

    static class InternalType0241
    implements MinecraftClientAccess {
        private InternalType0525 internalField0251;
        private boolean internalField0277;
        private final EventListener<ClientPlayerTickEvent> internalField0157 = clientPlayerTickEvent -> this.internalMethod04058();

        InternalType0241() {
        }

        void internalMethod02785(InventorySlot typedValue222, InventorySlot typedValue223) {
            this.internalField0251 = new InternalType0525(typedValue222, typedValue223);
            if (!this.internalField0277) {
                RockstarClient.getInstance().internalMethod03317().internalMethod00647(this);
                this.internalField0277 = true;
            }
        }

        private void internalMethod04058() {
            if (InternalType0241.internalField0149.player == null) {
                this.internalField0251 = null;
                this.internalMethod04062();
                return;
            }
            if (this.internalField0251 == null) {
                this.internalMethod04062();
                return;
            }
            GuiMoveModule typedValue273 = RockstarClient.getInstance().getModuleManager().getModule(GuiMoveModule.class);
            if (this.internalField0251.internalField0022.internalMethod06662() >= 36 && this.internalField0251.internalField0022.internalMethod06662() <= 44) {
                InventoryUtils.internalMethod08821(this.internalField0251.internalField0023.internalMethod06662(), this.internalField0251.internalField0022.internalMethod06662() - 36);
                this.internalField0251 = null;
            } else if (ServerUtils.internalMethod01786(KnownServer.internalField0578)) {
                if (this.internalField0251.internalField0227 == 0 && typedValue273.internalMethod06994().isEmpty()) {
                    InventoryUtils.internalMethod08821(this.internalField0251.internalField0022.internalMethod06662(), 8);
                    InventoryUtils.internalMethod08821(this.internalField0251.internalField0023.internalMethod06662(), 8);
                    InventoryUtils.internalMethod08821(this.internalField0251.internalField0022.internalMethod06662(), 8);
                    ++this.internalField0251.internalField0227;
                } else if (this.internalField0251.internalField0227 == 1 && typedValue273.internalMethod06994().isEmpty()) {
                    ++this.internalField0251.internalField0227;
                } else if (this.internalField0251.internalField0227 == 2 && typedValue273.internalMethod06994().isEmpty()) {
                    ++this.internalField0251.internalField0227;
                }
            } else if (this.internalField0251.internalField0227 == 0 && typedValue273.internalMethod06994().isEmpty()) {
                InventoryUtils.internalMethod08821(this.internalField0251.internalField0022.internalMethod06662(), 8);
                ++this.internalField0251.internalField0227;
            } else if (this.internalField0251.internalField0227 == 1 && typedValue273.internalMethod06994().isEmpty()) {
                InventoryUtils.internalMethod08821(this.internalField0251.internalField0023.internalMethod06662(), 8);
                ++this.internalField0251.internalField0227;
            } else if (this.internalField0251.internalField0227 == 2 && typedValue273.internalMethod06994().isEmpty()) {
                InventoryUtils.internalMethod08821(this.internalField0251.internalField0022.internalMethod06662(), 8);
                ++this.internalField0251.internalField0227;
            }
            if (this.internalField0251 != null && this.internalField0251.internalField0227 >= 3) {
                this.internalField0251 = null;
            }
            if (this.internalField0251 == null) {
                this.internalMethod04062();
            }
        }

        private void internalMethod04062() {
            if (this.internalField0277) {
                RockstarClient.getInstance().internalMethod03317().internalMethod07237(this);
                this.internalField0277 = false;
            }
        }

        static class InternalType0525 {
            int internalField0227;
            final InventorySlot internalField0022;
            final InventorySlot internalField0023;

            InternalType0525(InventorySlot typedValue222, InventorySlot typedValue223) {
                this.internalField0022 = typedValue222;
                this.internalField0023 = typedValue223;
            }
        }
    }
}
