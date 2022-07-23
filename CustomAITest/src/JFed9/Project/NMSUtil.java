package JFed9.Project;

import net.minecraft.world.entity.EntityInsentient;
import org.bukkit.entity.EntityType;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NMSUtil {

    public void registerEntity(String name, int id, Class<? extends EntityInsentient> nmsclass, Class<? extends EntityInsentient> customClass) {
        try {

            List<Map<?,?>> dataMap = new ArrayList<>();

            for (Field f : EntityType.class.getDeclaredFields()) {
                if (f.getType().getSimpleName().equals(Map.class.getSimpleName())) {
                    f.setAccessible(true);
                    dataMap.add((Map<?,?>)f.get(null));
                }
            }

            if (dataMap.get(2).containsKey(id)) {
                dataMap.get(0).remove(name);
                dataMap.get(2).remove(id);
            }

            Method method = EntityType.class.getDeclaredMethod("a",Class.class, String.class, int.class);

            method.setAccessible(true);
            method.invoke(null, customClass, name, id);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
