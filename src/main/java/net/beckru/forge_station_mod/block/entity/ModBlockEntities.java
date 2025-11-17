package net.beckru.forge_station_mod.block.entity;

import net.beckru.forge_station_mod.ForgeStation;
import net.beckru.forge_station_mod.block.ModBlocks;
import net.beckru.forge_station_mod.block.entity.custom.ForgeStationBasicEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, ForgeStation.MOD_ID);

    public static final RegistryObject<BlockEntityType<ForgeStationBasicEntity>> FORGESTATIONBASIC_BE =
            BLOCK_ENTITIES.register("forge_station_basic_be", () -> BlockEntityType.Builder.of(
                    ForgeStationBasicEntity::new, ModBlocks.FORGESTATIONBASIC.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}

