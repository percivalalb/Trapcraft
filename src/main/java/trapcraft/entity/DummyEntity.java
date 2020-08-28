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

	private static final DataParameter<Byte> VARIANT = EntityDataManager.<Byte>createKey(DummyEntity.class, DataSerializers.BYTE);

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
        return MobEntity.func_233666_p_().createMutableAttribute(Attributes.MAX_HEALTH, 20.0D).createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.3F).createMutableAttribute(Attributes.KNOCKBACK_RESISTANCE, 1.0D).createMutableAttribute(ForgeMod.NAMETAG_DISTANCE.get(), 4.0D);
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(VARIANT, Byte.valueOf((byte)0));
    }

    @Override
    protected void jump() {}

    @Override
    public void livingTick() {
        this.setMotion(0.0D, this.getMotion().getY(), 0.0D);
        super.livingTick();
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putByte("variant", this.getVariant());
    }

    @Override
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        this.setVariant(compound.getByte("variant"));
    }

    public void setVariant(final byte index) {
    	this.dataManager.set(VARIANT, index);
    }

    public byte getVariant() {
    	return this.dataManager.get(VARIANT);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean getAlwaysRenderNameTagForRender() {
       return false;
    }

	@Override
	public Iterable<ItemStack> getArmorInventoryList() {
		return Collections.emptyList();
	}

	@Override
	public ItemStack getItemStackFromSlot(EquipmentSlotType slotIn) {
		return ItemStack.EMPTY;
	}

	@Override
	public void setItemStackToSlot(EquipmentSlotType slotIn, ItemStack stack) {

	}

	@Override
	public HandSide getPrimaryHand() {
		return HandSide.RIGHT;
	}

	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}
