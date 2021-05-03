package com.railwayteam.railways.items;

import com.railwayteam.railways.ModSetup;
import com.railwayteam.railways.entities.conductor.ConductorEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.minecart.MinecartEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ConductorItem extends Item {
    public static ItemStack create(Entity entity) {
        ItemStack stack = new ItemStack(ModSetup.R_ITEM_CONDUCTOR.get());
        putEntityDataInItem(stack, entity);
        return stack;
    }

    public ConductorItem(Properties p_i48487_1_) {
        super(p_i48487_1_);
    }

    public static ConductorEntity spawn(ItemStack stack, BlockPos pos, PlayerEntity plr) {
        if(!hasEntity(stack) || stack.getOrCreateTag().getBoolean("spawn_entity")) { // if the item is created using /give or through creative, it doesnt have an entity
            if(!plr.isCreative()) {
                stack.setCount(stack.getCount()-1);
            }
            return ConductorEntity.spawn(plr.world, pos);
        } else {
            CompoundNBT tag = stack.getTag();
            tag.putBoolean("spawn_entity", true);// for some reason setCount doesnt work in creative, so im doing this
            stack.setTag(tag);
            stack.setCount(0);
            LivingEntity entity = (LivingEntity) getEntityFromItem(stack, plr.world);
            entity.setHealth(entity.getMaxHealth());
            plr.world.addEntity(entity);
            entity.setPositionAndUpdate(pos.getX(), pos.getY(), pos.getZ());
            entity.fallDistance = 0; // prevent instant death if died from fall damage
            entity.setFireTicks(0); // prevent fire if died from fire
            entity.setAir(entity.getMaxAir()); // prevent starting to drown immediately if died from drowning
            entity.setMotion(0, 0.1, 0);
            return (ConductorEntity) entity;
        }
    }

    public static ActionResultType onMinecartRightClicked(PlayerEntity plr, ItemStack stack, Hand hand, MinecartEntity entity) {
        ConductorEntity conductor = spawn(stack, entity.getBlockPos(), plr);
        conductor.startRiding(entity);

        return ActionResultType.SUCCESS;
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext ctx) {
        World world = ctx.getWorld();
        ItemStack stack = ctx.getItem();
        PlayerEntity plr = ctx.getPlayer();
        BlockPos pos = ctx.getPos();
        return ActionResultType.SUCCESS;
    }

    @Override
    public void onCreated(ItemStack stack, World world, PlayerEntity player) {
        super.onCreated(stack, world, player);
        stack.setTag(new CompoundNBT());
    }

    public static boolean hasEntity(CompoundNBT nbt)  {
        return nbt.contains("entity");
    }

    public static boolean hasEntity(ItemStack stack) {
        return hasEntity(stack.getOrCreateTag());
    }

    public static void putEntityDataInNbt(CompoundNBT nbt, Entity entity) {
        nbt.putString("entity", EntityType.getKey(entity.getType()).toString());
        entity.writeWithoutTypeId(nbt);
    }

    public static void putEntityDataInItem(ItemStack item, Entity entity) {
        putEntityDataInNbt(item.getOrCreateTag(), entity);
    }

    public static Entity getEntityFromNBT(CompoundNBT nbt, World world) {
        EntityType type = EntityType.byKey(nbt.getString("entity")).orElse(null);
        if (type != null) {
            Entity entity = type.create(world);
            entity.read(nbt);
            return entity;
        }
        return null;
    }

    public static Entity getEntityFromItem(ItemStack item, World world) {
        return getEntityFromNBT(item.getOrCreateTag(), world);
    }

    public static CompoundNBT getOrCreateNbt(ItemStack stack) {
        return stack.getOrCreateTag();
    }
}