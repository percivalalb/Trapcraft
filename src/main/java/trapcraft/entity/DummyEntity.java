package trapcraft.entity;

import java.util.Collections;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.fmllegacy.network.FMLPlayMessages.SpawnEntity;
import net.minecraftforge.fmllegacy.network.NetworkHooks;
import trapcraft.TrapcraftEntityTypes;

/**
 * @author ProPercivalalb
 **/
public class DummyEntity extends LivingEntity {

    private static final EntityDataAccessor<Byte> VARIANT = SynchedEntityData.<Byte>defineId(DummyEntity.class, EntityDataSerializers.BYTE);

    public DummyEntity(SpawnEntity packet, Level world) {
        this(world);
    }

    public DummyEntity(Level world) {
        this(TrapcraftEntityTypes.DUMMY.get(), world);
    }

    public DummyEntity(EntityType<? extends LivingEntity> type, Level world) {
        super(type, world);
    }

    public static AttributeSupplier.Builder createAttributeMap() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 20.0D).add(Attributes.MOVEMENT_SPEED, 0.3F).add(Attributes.KNOCKBACK_RESISTANCE, 1.0D).add(ForgeMod.NAMETAG_DISTANCE.get(), 4.0D);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(VARIANT, Byte.valueOf((byte)0));
    }

    @Override
    protected void jumpFromGround() {}

    @Override
    public void aiStep() {
        this.setDeltaMovement(0.0D, this.getDeltaMovement().y(), 0.0D);
        super.aiStep();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putByte("variant", this.getVariant());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setVariant(compound.getByte("variant"));
    }

    public void setVariant(final byte index) {
        this.entityData.set(VARIANT, index);
    }

    public byte getVariant() {
        return this.entityData.get(VARIANT);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean shouldShowName() {
       return false;
    }

    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return Collections.emptyList();
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlot slotIn) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemSlot(EquipmentSlot slotIn, ItemStack stack) {

    }

    @Override
    public HumanoidArm getMainArm() {
        return HumanoidArm.RIGHT;
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
