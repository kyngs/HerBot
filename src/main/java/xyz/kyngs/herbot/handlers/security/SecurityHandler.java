package xyz.kyngs.herbot.handlers.security;

import xyz.kyngs.herbot.HerBot;
import xyz.kyngs.herbot.handlers.AbstractHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SecurityHandler extends AbstractHandler {
    private final Map<String, Permission> loadedPermissions;

    public SecurityHandler(HerBot herBot) {
        super(herBot);
        loadedPermissions = new HashMap<>();
        loadPermissions();
        registerPermission("*", "Administrátor - Povoluje přístup ke všemu");
        registerPermission("ManagePermissions", "Správce Oprávnění - Povoluje přidání a odebrání oprávnění");
    }

    public Map<String, Permission> getLoadedPermissions() {
        return loadedPermissions;
    }

    private void loadPermissions() {
        herBot.getDatabaseManager().getMySQL().sync().schedule(connection -> {
            var rs = connection.prepareStatement("SELECT * FROM permissions").executeQuery();
            while (rs.next()) {
                var permission = new Permission(rs.getString("name"), rs.getString("description"));
                loadedPermissions.put(permission.getName(), permission);
            }
        });
    }

    private void registerPermission(String name, String description) {
        if (loadedPermissions.get(name) != null) return;
        var permission = new Permission(name, description);
        loadedPermissions.put(name, permission);
        herBot.getDatabaseManager().getMySQL().sync().schedule(connection -> {
            var ps = connection.prepareStatement("INSERT INTO permissions (name, description) VALUES (?, ?)");
            ps.setString(1, name);
            ps.setString(2, description);
            ps.execute();
        });
    }

    public Permission getPermissionFromName(String name) {
        return loadedPermissions.get(name);
    }

    public List<Permission> getPermissionsFromNames(List<String> names) {
        var list = new ArrayList<Permission>();
        for (var entry : loadedPermissions.entrySet()) {
            if (names.contains(entry.getKey())) {
                list.add(entry.getValue());
            }
        }
        return list;
    }

}
