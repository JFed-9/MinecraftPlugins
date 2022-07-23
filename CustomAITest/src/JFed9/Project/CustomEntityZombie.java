package JFed9.Project;

import net.minecraft.world.entity.ai.attributes.GenericAttributes;
import net.minecraft.world.entity.monster.EntityZombie;
import net.minecraft.world.level.World;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftLivingEntity;
import org.bukkit.entity.Zombie;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class CustomEntityZombie extends EntityZombie {

    public CustomEntityZombie(World world) {
        super(world);
    }

    protected void initAttributes() {
        super.initDatawatcher();

        this.getAttributeInstance(GenericAttributes.e).setValue(10.0D);
        this.getAttributeInstance(GenericAttributes.d).setValue(1.0D);
    }

    @Override
    public boolean doAITick() {
        return super.doAITick();
    }

    public static Zombie spawn(Location location) {
        World mcWorld = (World) ((CraftWorld) location.getWorld()).getHandle();
        final CustomEntityZombie customEntity = new CustomEntityZombie(mcWorld);

        customEntity.setLocation(location.getX(),location.getY(),location.getZ(),location.getYaw(),location.getPitch());

        ((CraftLivingEntity) customEntity.getBukkitEntity()).setRemoveWhenFarAway(false);

        mcWorld.addEntity(customEntity, CreatureSpawnEvent.SpawnReason.CUSTOM);

        return (Zombie) customEntity.getBukkitEntity();
    }
}
