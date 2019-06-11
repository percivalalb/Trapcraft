package trapcraft.entity;

import java.util.Collections;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
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
import net.minecraftforge.fml.network.FMLPlayMessages.SpawnEntity;
import net.minecraftforge.fml.network.NetworkHooks;
import trapcraft.ModEntities;

/**
 * @author ProPercivalalb
 **/
public class EntityDummy extends LivingEntity {
	
	private static final DataParameter<Byte> VARIANT = EntityDataManager.<Byte>createKey(EntityDummy.class, DataSerializers.BYTE);
   
	public EntityDummy(SpawnEntity packet, World world) {
		this(world);
	}
	
    public EntityDummy(World world) {
        this(ModEntities.DUMMY, world);
    }
    
    public EntityDummy(EntityType<?> type, World world) {
        super(ModEntities.DUMMY, world);
    }

    @Override
	protected void registerAttributes() {
	    super.registerAttributes();
	    this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0D);
	    this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.0D);
	    this.getAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
	    this.getAttribute(NAMETAG_DISTANCE).setBaseValue(4D);
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
        this.randomYawVelocity = 0.0F;
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
    
    public void setVariant(byte index) {
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
