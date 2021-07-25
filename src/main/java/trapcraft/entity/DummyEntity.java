package trapcraft.entity;

import java.util.Collections;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.HandSide;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.fml.network.FMLPlayMessages.SpawnEntity;
import net.minecraftforge.fml.network.NetworkHooks;
import trapcraft.TrapcraftEntityTypes;

/**
 * @author ProPercivalalb
 **/
public class DummyEntity extends LivingEntity {

    private static final DataParameter<Byte> VARIANT = EntityDataManager.<Byte>defineId(DummyEntity.class, DataSerializers.BYTE);

    public DummyEntity(SpawnEntity packet, World world) {
        this(world);
    }

    public DummyEntity(World world) {
        this(TrapcraftEntityTypes.DUMMY.get(), world);
    }

    public DummyEntity(EntityType<? extends LivingEntity> type, World world) {
        super(type, world);
    }

    public static AttributeModifierMap.MutableAttribute createAttributeMap() {
        return MobEntity.createMobAttributes().add(Attributes.MAX_HEALTH, 20.0D).add(Attributes.MOVEMENT_SPEED, 0.3F).add(Attributes.KNOCKBACK_RESISTANCE, 1.0D).add(ForgeMod.NAMETAG_DISTANCE.get(), 4.0D);
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
    public void addAdditionalSaveData(CompoundNBT compound) {
        super.addAdditionalSaveData(compound);
        compound.putByte("variant", this.getVariant());
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT compound) {
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
    public ItemStack getItemBySlot(EquipmentSlotType slotIn) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemSlot(EquipmentSlotType slotIn, ItemStack stack) {

    }

    @Override
    public HandSide getMainArm() {
        return HandSide.RIGHT;
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
