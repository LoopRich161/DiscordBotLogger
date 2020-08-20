package ru.frostdelta.discord.fake;

import org.bukkit.*;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.block.data.BlockData;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.entity.*;
import org.bukkit.entity.memory.MemoryKey;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.*;
import org.bukkit.map.MapView;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.frostdelta.discord.Util;
import ru.looprich.discordlogger.DiscordLogger;
import ru.looprich.discordlogger.module.DiscordBot;

import java.net.InetSocketAddress;
import java.util.*;

public class FakePlayer extends FakePlayerCommandSender implements Player {

    private final OfflinePlayer offlinePlayer;
    private final Player player;
    private final net.milkbowl.vault.permission.Permission permission;
    private final String offlineName;
    private final PluginManager pluginManager = Bukkit.getPluginManager();
    private final FakePlayer thisFakePlayer = this;

    public FakePlayer(String name) {
        super(name);
        offlinePlayer = super.getOfflinePlayer();
        player = super.getPlayer();
        permission = FakePlayerPermissionManager.getFakePlayerPermissions();
        offlineName = name;
    }

    public FakePlayer(UUID uuid) {
        super(uuid);
        offlinePlayer = super.getOfflinePlayer();
        player = super.getPlayer();
        permission = FakePlayerPermissionManager.getFakePlayerPermissions();
        offlineName = offlinePlayer.getName();
    }

    private String sendError() {
        DiscordBot.sendImportantMessage("Ошибка, действие недоступно!");
        return null;
    }

    @Override
    public String getDisplayName() {
        if (isOnline()) {
            return player.getName();
        } else return offlineName;
    }

    @Override
    public void setDisplayName(String name) {
        sendError();
    }

    @Override
    public String getPlayerListName() {
        if (isOnline()) {
            return player.getPlayerListName();
        } else return sendError();
    }

    @Override
    public void setPlayerListName(String name) {
        sendError();
    }

    /**
     * Gets the currently displayed player list header for this player.
     *
     * @return player list header or null
     */
    @Override
    public @Nullable String getPlayerListHeader() {
        return null;
    }

    /**
     * Gets the currently displayed player list footer for this player.
     *
     * @return player list header or null
     */
    @Override
    public @Nullable String getPlayerListFooter() {
        return null;
    }

    /**
     * Sets the currently displayed player list header for this player.
     *
     * @param header player list header, null for empty
     */
    @Override
    public void setPlayerListHeader(@Nullable String header) {

    }

    /**
     * Sets the currently displayed player list footer for this player.
     *
     * @param footer player list footer, null for empty
     */
    @Override
    public void setPlayerListFooter(@Nullable String footer) {

    }

    /**
     * Sets the currently displayed player list header and footer for this
     * player.
     *
     * @param header player list header, null for empty
     * @param footer player list footer, null for empty
     */
    @Override
    public void setPlayerListHeaderFooter(@Nullable String header, @Nullable String footer) {

    }

    @Override
    public void setCompassTarget(Location loc) {
        sendError();
    }

    @Override
    public Location getCompassTarget() {
        if (isOnline()) {
            return player.getCompassTarget();
        } else {
            sendError();
            return null;
        }
    }

    @Override
    public InetSocketAddress getAddress() {
        if (isOnline()) {
            return player.getAddress();
        } else {
            sendError();
            return null;
        }
    }

    @Override
    public boolean isConversing() {
        if (isOnline()) {
            return player.isConversing();
        } else return false;
    }

    @Override
    public void acceptConversationInput(String input) {
        sendError();
    }

    @Override
    public boolean beginConversation(Conversation conversation) {
        if (isOnline()) {
            return player.beginConversation(conversation);
        } else return false;
    }

    @Override
    public void abandonConversation(Conversation conversation) {

    }

    @Override
    public void abandonConversation(Conversation conversation, ConversationAbandonedEvent details) {

    }

    @Override
    public void sendRawMessage(String message) {
        if (player.isOnline()) {
            player.sendRawMessage(message);
        }
    }

    @Override
    public void kickPlayer(String message) {
        if (isOnline()) {
            player.kickPlayer(message);
        } else sendError();
    }

    @Override
    public void chat(@NotNull String msg) {
        if (isOnline()) {
            player.chat(msg);
        } else
            DiscordLogger.getInstance().getServer().getScheduler().runTaskAsynchronously(DiscordLogger.getInstance(), () -> pluginManager.callEvent(new AsyncPlayerChatEvent(true, thisFakePlayer, msg, getOnlinePlayerSet(msg))));
    }

    private Set<Player> getOnlinePlayerSet(@NotNull String msg) {
        if (msg.startsWith("!")) {
            return new HashSet<>(Bukkit.getOnlinePlayers());
        } else {
            Set<Player> playerSet = new HashSet<>();
            double maxDist = DiscordLogger.getInstance().getConfig().getDouble("local-chat-radius");
            for (Player other : Bukkit.getOnlinePlayers()) {
                if (other.getLocation().getWorld().getUID().equals(this.getWorld().getUID()) && other.getLocation().distance(this.getLocation()) <= maxDist) {
                    playerSet.add(other);
                }
            }
            return playerSet;
        }
    }

    @Override
    public boolean performCommand(String command) {
        return false;
    }

    @Override
    public boolean isSneaking() {
        return false;
    }

    @Override
    public void setSneaking(boolean sneak) {
        sendError();
    }

    @Override
    public boolean isSprinting() {
        return false;
    }

    @Override
    public void setSprinting(boolean sprinting) {

    }

    @Override
    public void saveData() {

    }

    @Override
    public void loadData() {

    }

    @Override
    public void setSleepingIgnored(boolean isSleeping) {

    }

    @Override
    public boolean isSleepingIgnored() {
        return false;
    }

    @Override
    public void playNote(Location loc, byte instrument, byte note) {

    }

    @Override
    public void playNote(Location loc, Instrument instrument, Note note) {

    }

    @Override
    public void playSound(Location location, Sound sound, float volume, float pitch) {

    }

    @Override
    public void playSound(Location location, String sound, float volume, float pitch) {

    }

    @Override
    public void playSound(Location location, Sound sound, SoundCategory category, float volume, float pitch) {

    }

    @Override
    public void playSound(Location location, String sound, SoundCategory category, float volume, float pitch) {

    }

    @Override
    public void stopSound(Sound sound) {

    }

    @Override
    public void stopSound(String sound) {

    }

    @Override
    public void stopSound(Sound sound, SoundCategory category) {

    }

    @Override
    public void stopSound(String sound, SoundCategory category) {

    }

    @Override
    public void playEffect(Location loc, Effect effect, int data) {

    }

    @Override
    public <T> void playEffect(Location loc, Effect effect, T data) {

    }

    @Override
    public void sendBlockChange(Location loc, Material material, byte data) {

    }

    /**
     * Send a block change. This fakes a block change packet for a user at a
     * certain location. This will not actually change the world in any way.
     *
     * @param loc   The location of the changed block
     * @param block The new block
     */
    @Override
    public void sendBlockChange(@NotNull Location loc, @NotNull BlockData block) {

    }

    @Override
    public boolean sendChunkChange(Location loc, int sx, int sy, int sz, byte[] data) {
        return false;
    }

    @Override
    public void sendSignChange(Location loc, String[] lines) throws IllegalArgumentException {

    }

    @Override
    public void sendMap(MapView map) {

    }

    @Override
    public void updateInventory() {

    }

    @Override
    public void awardAchievement(Achievement achievement) {

    }

    @Override
    public void removeAchievement(Achievement achievement) {

    }

    @Override
    public boolean hasAchievement(Achievement achievement) {
        return false;
    }

    @Override
    public void incrementStatistic(Statistic statistic) throws IllegalArgumentException {

    }

    @Override
    public void decrementStatistic(Statistic statistic) throws IllegalArgumentException {

    }

    @Override
    public void incrementStatistic(Statistic statistic, int amount) throws IllegalArgumentException {

    }

    @Override
    public void decrementStatistic(Statistic statistic, int amount) throws IllegalArgumentException {

    }

    @Override
    public void setStatistic(Statistic statistic, int newValue) throws IllegalArgumentException {

    }

    @Override
    public int getStatistic(Statistic statistic) throws IllegalArgumentException {
        return 0;
    }

    @Override
    public void incrementStatistic(Statistic statistic, Material material) throws IllegalArgumentException {

    }

    @Override
    public void decrementStatistic(Statistic statistic, Material material) throws IllegalArgumentException {

    }

    @Override
    public int getStatistic(Statistic statistic, Material material) throws IllegalArgumentException {
        return 0;
    }

    @Override
    public void incrementStatistic(Statistic statistic, Material material, int amount) throws IllegalArgumentException {

    }

    @Override
    public void decrementStatistic(Statistic statistic, Material material, int amount) throws IllegalArgumentException {

    }

    @Override
    public void setStatistic(Statistic statistic, Material material, int newValue) throws IllegalArgumentException {

    }

    @Override
    public void incrementStatistic(@NotNull Statistic statistic, EntityType entityType) throws IllegalArgumentException {

    }

    @Override
    public void decrementStatistic(Statistic statistic, EntityType entityType) throws IllegalArgumentException {

    }

    @Override
    public int getStatistic(Statistic statistic, EntityType entityType) throws IllegalArgumentException {
        return 0;
    }

    @Override
    public void incrementStatistic(Statistic statistic, EntityType entityType, int amount) throws IllegalArgumentException {

    }

    @Override
    public void decrementStatistic(Statistic statistic, EntityType entityType, int amount) {

    }

    @Override
    public void setStatistic(Statistic statistic, EntityType entityType, int newValue) {

    }

    @Override
    public void setPlayerTime(long time, boolean relative) {

    }

    @Override
    public long getPlayerTime() {
        return 0;
    }

    @Override
    public long getPlayerTimeOffset() {
        return 0;
    }

    @Override
    public boolean isPlayerTimeRelative() {
        return false;
    }

    @Override
    public void resetPlayerTime() {

    }

    @Override
    public void setPlayerWeather(WeatherType type) {

    }

    @Override
    public WeatherType getPlayerWeather() {
        return null;
    }

    @Override
    public void resetPlayerWeather() {

    }

    @Override
    public void giveExp(int amount) {

    }

    @Override
    public void giveExpLevels(int amount) {

    }

    @Override
    public float getExp() {
        return 0;
    }

    @Override
    public void setExp(float exp) {

    }

    @Override
    public int getLevel() {
        return 0;
    }

    @Override
    public void setLevel(int level) {

    }

    @Override
    public int getTotalExperience() {
        return 0;
    }

    @Override
    public void setTotalExperience(int exp) {

    }

    @Override
    public float getExhaustion() {
        return 0;
    }

    @Override
    public void setExhaustion(float value) {

    }

    @Override
    public float getSaturation() {
        return 0;
    }

    @Override
    public void setSaturation(float value) {

    }

    @Override
    public int getFoodLevel() {
        return 0;
    }

    @Override
    public void setFoodLevel(int value) {

    }

    @Override
    public boolean isOnline() {
        return super.isOnline();
    }

    @Override
    public boolean isBanned() {
        return false;
    }

    @Override
    public boolean isWhitelisted() {
        return offlinePlayer.isWhitelisted();
    }

    @Override
    public void setWhitelisted(boolean value) {
        offlinePlayer.setWhitelisted(value);
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public long getFirstPlayed() {
        return offlinePlayer.getFirstPlayed();
    }

    @Override
    public long getLastPlayed() {
        return offlinePlayer.getLastPlayed();
    }

    @Override
    public boolean hasPlayedBefore() {
        return offlinePlayer.hasPlayedBefore();
    }

    @Override
    public Location getBedSpawnLocation() {
        return offlinePlayer.getBedSpawnLocation();
    }

    @Override
    public void setBedSpawnLocation(Location location) {
        sendError();
    }

    @Override
    public void setBedSpawnLocation(Location location, boolean force) {
        sendError();
    }

    /**
     * Attempts to make the entity sleep at the given location.
     * <br>
     * The location must be in the current world and have a bed placed at the
     * location. The game may also enforce other requirements such as proximity
     * to bed, monsters, and dimension type if force is not set.
     *
     * @param location the location of the bed
     * @param force    whether to try and sleep at the location even if not
     *                 normally possible
     * @return whether the sleep was successful
     */
    @Override
    public boolean sleep(@NotNull Location location, boolean force) {
        return false;
    }

    /**
     * Causes the player to wakeup if they are currently sleeping.
     *
     * @param setSpawnLocation whether to set their spawn location to the bed
     *                         they are currently sleeping in
     * @throws IllegalStateException if not sleeping
     */
    @Override
    public void wakeup(boolean setSpawnLocation) {

    }

    /**
     * Gets the location of the bed the player is currently sleeping in
     *
     * @return location
     * @throws IllegalStateException if not sleeping
     */
    @Override
    public @NotNull Location getBedLocation() {
        return null;
    }

    @Override
    public boolean getAllowFlight() {
        if (isOnline()) {
            return player.getAllowFlight();
        }
        return false;
    }

    @Override
    public void setAllowFlight(boolean flight) {
        if (isOnline()) {
            player.setAllowFlight(flight);
        }
    }

    @Override
    public void hidePlayer(Player player) {
        if (isOnline()) {
            player.hidePlayer(player);
        } else sendError();
    }

    @Override
    public void hidePlayer(Plugin plugin, Player player) {
        if (isOnline()) {
            player.hidePlayer(plugin, player);
        } else sendError();
    }

    @Override
    public void showPlayer(Player player) {
        if (isOnline()) {
            player.hidePlayer(player);
        } else sendError();
    }

    @Override
    public void showPlayer(Plugin plugin, Player player) {
        if (isOnline()) {
            player.hidePlayer(plugin, player);
        } else sendError();
    }

    @Override
    public boolean canSee(Player player) {
        if (isOnline()) {
            return this.player.canSee(player);
        }
        return false;
    }

    @Override
    public boolean isFlying() {
        if (isOnline()) {
            return player.isFlying();
        } else return false;
    }

    @Override
    public void setFlying(boolean value) {
        if (isOnline()) {
            player.setFlying(value);
        } else sendError();
    }

    @Override
    public void setFlySpeed(float value) throws IllegalArgumentException {
        if (isOnline()) {
            player.setFlySpeed(value);
        } else sendError();
    }

    @Override
    public void setWalkSpeed(float value) throws IllegalArgumentException {
        if (isOnline()) {
            player.setWalkSpeed(value);
        } else sendError();
    }

    @Override
    public float getFlySpeed() {
        if (isOnline()) {
            player.getFlySpeed();
        }
        return 0;
    }

    @Override
    public float getWalkSpeed() {
        return 0;
    }

    @Override
    public void setTexturePack(String url) {

    }

    @Override
    public void setResourcePack(String url) {

    }

    @Override
    public void setResourcePack(String url, byte[] hash) {

    }

    @Override
    public Scoreboard getScoreboard() {
        return null;
    }

    @Override
    public void setScoreboard(Scoreboard scoreboard) throws IllegalArgumentException, IllegalStateException {

    }

    @Override
    public boolean isHealthScaled() {
        return false;
    }

    @Override
    public void setHealthScaled(boolean scale) {

    }

    @Override
    public void setHealthScale(double scale) throws IllegalArgumentException {

    }

    @Override
    public double getHealthScale() {
        return 0;
    }

    @Override
    public Entity getSpectatorTarget() {
        return null;
    }

    @Override
    public void setSpectatorTarget(Entity entity) {

    }

    @Override
    public void sendTitle(String title, String subtitle) {

    }

    @Override
    public void sendTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut) {

    }

    @Override
    public void resetTitle() {

    }

    @Override
    public void spawnParticle(Particle particle, Location location, int count) {

    }

    @Override
    public void spawnParticle(Particle particle, double x, double y, double z, int count) {

    }

    @Override
    public <T> void spawnParticle(Particle particle, Location location, int count, T data) {

    }

    @Override
    public <T> void spawnParticle(Particle particle, double x, double y, double z, int count, T data) {

    }

    @Override
    public void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ) {

    }

    @Override
    public void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ) {

    }

    @Override
    public <T> void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ, T data) {

    }

    @Override
    public <T> void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, T data) {

    }

    @Override
    public void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ, double extra) {

    }

    @Override
    public void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra) {

    }

    @Override
    public <T> void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ, double extra, T data) {

    }

    @Override
    public <T> void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra, T data) {

    }

    @Override
    public AdvancementProgress getAdvancementProgress(Advancement advancement) {
        return null;
    }

    /**
     * Get the player's current client side view distance.
     * <br>
     * Will default to the server view distance if the client has not yet
     * communicated this information,
     *
     * @return client view distance as above
     */
    @Override
    public int getClientViewDistance() {
        return 0;
    }

    @Override
    public String getLocale() {
        return null;
    }

    /**
     * Update the list of commands sent to the client.
     * <br>
     * Generally useful to ensure the client has a complete list of commands
     * after permission changes are done.
     */
    @Override
    public void updateCommands() {

    }

    @Override
    public void openBook(@NotNull ItemStack itemStack) {

    }

    @Override
    public Map<String, Object> serialize() {
        return null;
    }

    @Override
    public String getName() {
        if (isOnline()) {
            return player.getName();
        } else return offlineName;
    }

    @Override
    public PlayerInventory getInventory() {
        if (isOnline()) {
            return player.getInventory();
        } else {
            sendError();
            return null;
        }
    }

    @Override
    public Inventory getEnderChest() {
        return null;
    }

    @Override
    public MainHand getMainHand() {
        return null;
    }

    @Override
    public boolean setWindowProperty(InventoryView.Property prop, int value) {
        return false;
    }

    @Override
    public InventoryView getOpenInventory() {
        return null;
    }

    @Override
    public InventoryView openInventory(Inventory inventory) {
        return null;
    }

    @Override
    public InventoryView openWorkbench(Location location, boolean force) {
        return null;
    }

    @Override
    public InventoryView openEnchanting(Location location, boolean force) {
        return null;
    }

    @Override
    public void openInventory(InventoryView inventory) {

    }

    @Override
    public InventoryView openMerchant(Villager trader, boolean force) {
        return null;
    }

    @Override
    public InventoryView openMerchant(Merchant merchant, boolean force) {
        return null;
    }

    @Override
    public void closeInventory() {

    }

    @Override
    public ItemStack getItemInHand() {
        return null;
    }

    @Override
    public void setItemInHand(ItemStack item) {

    }

    @Override
    public ItemStack getItemOnCursor() {
        return null;
    }

    @Override
    public void setItemOnCursor(ItemStack item) {

    }

    @Override
    public boolean hasCooldown(Material material) {
        return false;
    }

    @Override
    public int getCooldown(Material material) {
        return 0;
    }

    @Override
    public void setCooldown(Material material, int ticks) {

    }

    @Override
    public boolean isSleeping() {
        return false;
    }

    @Override
    public int getSleepTicks() {
        return 0;
    }

    @Override
    public GameMode getGameMode() {
        return null;
    }

    @Override
    public void setGameMode(GameMode mode) {

    }

    @Override
    public boolean isBlocking() {
        return false;
    }

    @Override
    public boolean isHandRaised() {
        return false;
    }

    @Override
    public int getExpToLevel() {
        return 0;
    }

    /**
     * Discover a recipe for this player such that it has not already been
     * discovered. This method will add the key's associated recipe to the
     * player's recipe book.
     *
     * @param recipe the key of the recipe to discover
     * @return whether or not the recipe was newly discovered
     */
    @Override
    public boolean discoverRecipe(@NotNull NamespacedKey recipe) {
        return false;
    }

    /**
     * Discover a collection of recipes for this player such that they have not
     * already been discovered. This method will add the keys' associated
     * recipes to the player's recipe book. If a recipe in the provided
     * collection has already been discovered, it will be silently ignored.
     *
     * @param recipes the keys of the recipes to discover
     * @return the amount of newly discovered recipes where 0 indicates that
     * none were newly discovered and a number equal to {@code recipes.size()}
     * indicates that all were new
     */
    @Override
    public int discoverRecipes(@NotNull Collection<NamespacedKey> recipes) {
        return 0;
    }

    /**
     * Undiscover a recipe for this player such that it has already been
     * discovered. This method will remove the key's associated recipe from the
     * player's recipe book.
     *
     * @param recipe the key of the recipe to undiscover
     * @return whether or not the recipe was successfully undiscovered (i.e. it
     * was previously discovered)
     */
    @Override
    public boolean undiscoverRecipe(@NotNull NamespacedKey recipe) {
        return false;
    }

    /**
     * Undiscover a collection of recipes for this player such that they have
     * already been discovered. This method will remove the keys' associated
     * recipes from the player's recipe book. If a recipe in the provided
     * collection has not yet been discovered, it will be silently ignored.
     *
     * @param recipes the keys of the recipes to undiscover
     * @return the amount of undiscovered recipes where 0 indicates that none
     * were undiscovered and a number equal to {@code recipes.size()} indicates
     * that all were undiscovered
     */
    @Override
    public int undiscoverRecipes(@NotNull Collection<NamespacedKey> recipes) {
        return 0;
    }

    @Override
    public Entity getShoulderEntityLeft() {
        return null;
    }

    @Override
    public void setShoulderEntityLeft(Entity entity) {

    }

    @Override
    public Entity getShoulderEntityRight() {
        return null;
    }

    @Override
    public void setShoulderEntityRight(Entity entity) {

    }

    @Override
    public double getEyeHeight() {
        return 0;
    }

    @Override
    public double getEyeHeight(boolean ignorePose) {
        return 0;
    }

    @Override
    public Location getEyeLocation() {
        return null;
    }

    @Override
    public List<Block> getLineOfSight(Set<Material> transparent, int maxDistance) {
        return null;
    }

    @Override
    public Block getTargetBlock(Set<Material> transparent, int maxDistance) {
        return null;
    }

    @Override
    public List<Block> getLastTwoTargetBlocks(Set<Material> transparent, int maxDistance) {
        return null;
    }

    /**
     * Gets the block that the living entity has targeted.
     * <p>
     * This takes the blocks' precise collision shapes into account. Fluids are
     * ignored.
     * <p>
     * This may cause loading of chunks! Some implementations may impose
     * artificial restrictions on the maximum distance.
     *
     * @param maxDistance the maximum distance to scan
     * @return block that the living entity has targeted
     * @see #getTargetBlockExact(int, FluidCollisionMode)
     */
    @Override
    public @Nullable Block getTargetBlockExact(int maxDistance) {
        return null;
    }

    /**
     * Gets the block that the living entity has targeted.
     * <p>
     * This takes the blocks' precise collision shapes into account.
     * <p>
     * This may cause loading of chunks! Some implementations may impose
     * artificial restrictions on the maximum distance.
     *
     * @param maxDistance        the maximum distance to scan
     * @param fluidCollisionMode the fluid collision mode
     * @return block that the living entity has targeted
     * @see #rayTraceBlocks(double, FluidCollisionMode)
     */
    @Override
    public @Nullable Block getTargetBlockExact(int maxDistance, @NotNull FluidCollisionMode fluidCollisionMode) {
        return null;
    }

    /**
     * Performs a ray trace that provides information on the targeted block.
     * <p>
     * This takes the blocks' precise collision shapes into account. Fluids are
     * ignored.
     * <p>
     * This may cause loading of chunks! Some implementations may impose
     * artificial restrictions on the maximum distance.
     *
     * @param maxDistance the maximum distance to scan
     * @return information on the targeted block, or <code>null</code> if there
     * is no targeted block in range
     * @see #rayTraceBlocks(double, FluidCollisionMode)
     */
    @Override
    public @Nullable RayTraceResult rayTraceBlocks(double maxDistance) {
        return null;
    }

    /**
     * Performs a ray trace that provides information on the targeted block.
     * <p>
     * This takes the blocks' precise collision shapes into account.
     * <p>
     * This may cause loading of chunks! Some implementations may impose
     * artificial restrictions on the maximum distance.
     *
     * @param maxDistance        the maximum distance to scan
     * @param fluidCollisionMode the fluid collision mode
     * @return information on the targeted block, or <code>null</code> if there
     * is no targeted block in range
     * @see World#rayTraceBlocks(Location, Vector, double, FluidCollisionMode)
     */
    @Override
    public @Nullable RayTraceResult rayTraceBlocks(double maxDistance, @NotNull FluidCollisionMode fluidCollisionMode) {
        return null;
    }

    @Override
    public int getRemainingAir() {
        return 0;
    }

    @Override
    public void setRemainingAir(int ticks) {

    }

    @Override
    public int getMaximumAir() {
        return 0;
    }

    @Override
    public void setMaximumAir(int ticks) {

    }

    @Override
    public int getMaximumNoDamageTicks() {
        return 0;
    }

    @Override
    public void setMaximumNoDamageTicks(int ticks) {

    }

    @Override
    public double getLastDamage() {
        return 0;
    }

    @Override
    public void setLastDamage(double damage) {

    }

    @Override
    public int getNoDamageTicks() {
        return 0;
    }

    @Override
    public void setNoDamageTicks(int ticks) {

    }

    @Override
    public Player getKiller() {
        return null;
    }

    @Override
    public boolean addPotionEffect(PotionEffect effect) {
        return false;
    }

    @Override
    public boolean addPotionEffect(PotionEffect effect, boolean force) {
        return false;
    }

    @Override
    public boolean addPotionEffects(Collection<PotionEffect> effects) {
        return false;
    }

    @Override
    public boolean hasPotionEffect(PotionEffectType type) {
        return false;
    }

    @Override
    public PotionEffect getPotionEffect(PotionEffectType type) {
        return null;
    }

    @Override
    public void removePotionEffect(PotionEffectType type) {

    }

    @Override
    public Collection<PotionEffect> getActivePotionEffects() {
        return null;
    }

    @Override
    public boolean hasLineOfSight(Entity other) {
        return false;
    }

    @Override
    public boolean getRemoveWhenFarAway() {
        return false;
    }

    @Override
    public void setRemoveWhenFarAway(boolean remove) {

    }

    @Override
    public EntityEquipment getEquipment() {
        return null;
    }

    @Override
    public void setCanPickupItems(boolean pickup) {

    }

    @Override
    public boolean getCanPickupItems() {
        return false;
    }

    @Override
    public boolean isLeashed() {
        return false;
    }

    @Override
    public Entity getLeashHolder() throws IllegalStateException {
        return null;
    }

    @Override
    public boolean setLeashHolder(Entity holder) {
        return false;
    }

    @Override
    public boolean isGliding() {
        return false;
    }

    @Override
    public void setGliding(boolean gliding) {

    }

    /**
     * Checks to see if an entity is swimming.
     *
     * @return True if this entity is swimming.
     */
    @Override
    public boolean isSwimming() {
        return false;
    }

    /**
     * Makes entity start or stop swimming.
     * <p>
     * This may have unexpected results if the entity is not in water.
     *
     * @param swimming True if the entity is swimming.
     */
    @Override
    public void setSwimming(boolean swimming) {

    }

    /**
     * Checks to see if an entity is currently using the Riptide enchantment.
     *
     * @return True if this entity is currently riptiding.
     */
    @Override
    public boolean isRiptiding() {
        return false;
    }

    @Override
    public void setAI(boolean ai) {

    }

    @Override
    public boolean hasAI() {
        return false;
    }

    @Override
    public void setCollidable(boolean collidable) {

    }

    @Override
    public boolean isCollidable() {
        return false;
    }

    @Override
    public <T> @Nullable T getMemory(@NotNull MemoryKey<T> memoryKey) {
        return null;
    }

    @Override
    public <T> void setMemory(@NotNull MemoryKey<T> memoryKey, @Nullable T t) {

    }

    @Override
    public AttributeInstance getAttribute(Attribute attribute) {
        return null;
    }

    @Override
    public void damage(double amount) {

    }

    @Override
    public void damage(double amount, Entity source) {

    }

    @Override
    public double getHealth() {
        return 0;
    }

    @Override
    public void setHealth(double health) {

    }

    @Override
    public double getMaxHealth() {
        return 0;
    }

    @Override
    public void setMaxHealth(double health) {

    }

    @Override
    public void resetMaxHealth() {

    }

    @Override
    public Location getLocation() {
        if (player != null)
            return player.getLocation();
        else return new Location(Bukkit.getWorlds().get(0), 1, 2, 3); //TODO ДОДЕЛАТЬ (ПАРСИТЬ ИНФУ ИЗ uuid.dat ? )
    }

    @Override
    public Location getLocation(Location loc) {
        if (player != null)
            return player.getLocation();
        else return new Location(Bukkit.getWorlds().get(0), 1, 2, 3); //TODO ДОДЕЛАТЬ (ПАРСИТЬ ИНФУ ИЗ uuid.dat ? )
    }

    @Override
    public void setVelocity(Vector velocity) {

    }

    @Override
    public Vector getVelocity() {
        return null;
    }

    @Override
    public double getHeight() {
        return 0;
    }

    @Override
    public double getWidth() {
        return 0;
    }

    /**
     * Gets the entity's current bounding box.
     * <p>
     * The returned bounding box reflects the entity's current location and
     * size.
     *
     * @return the entity's current bounding box
     */
    @Override
    public @NotNull BoundingBox getBoundingBox() {
        return null;
    }

    @Override
    public boolean isOnGround() {
        return false;
    }

    @Override
    public World getWorld() {
        return getLocation().getWorld();
    }

    /**
     * Sets the entity's rotation.
     * <p>
     * Note that if the entity is affected by AI, it may override this rotation.
     *
     * @param yaw   the yaw
     * @param pitch the pitch
     * @throws UnsupportedOperationException if used for players
     * @deprecated draft API
     */
    @Override
    public void setRotation(float yaw, float pitch) {

    }

    @Override
    public boolean teleport(Location location) {
        return false;
    }

    @Override
    public boolean teleport(Location location, PlayerTeleportEvent.TeleportCause cause) {
        return false;
    }

    @Override
    public boolean teleport(Entity destination) {
        return false;
    }

    @Override
    public boolean teleport(Entity destination, PlayerTeleportEvent.TeleportCause cause) {
        return false;
    }

    @Override
    public List<Entity> getNearbyEntities(double x, double y, double z) {
        return new ArrayList<Entity>(getOnlinePlayerSet(""));
    }

    @Override
    public int getEntityId() {
        return 0;
    }

    @Override
    public int getFireTicks() {
        return 0;
    }

    @Override
    public int getMaxFireTicks() {
        return 0;
    }

    @Override
    public void setFireTicks(int ticks) {

    }

    @Override
    public void remove() {

    }

    @Override
    public boolean isDead() {
        return false;
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public void sendMessage(String message) {
        if (isOnline()) {
            player.sendMessage(message);
        } else DiscordBot.sendServerResponse(Util.removeCodeColors(message));
    }

    @Override
    public void sendMessage(String[] messages) {
        if (isOnline()) {
            for (String message : messages) {
                player.sendMessage(message);
            }
        } else DiscordBot.sendServerResponse(Util.removeCodeColors(messages));
    }

    @Override
    public Server getServer() {
        return Bukkit.getServer();
    }

    /**
     * Returns true if the entity gets persisted.
     * <p>
     * By default all entities are persistent. An entity will also not get
     * persisted, if it is riding an entity that is not persistent.
     * <p>
     * The persistent flag on players controls whether or not to save their
     * playerdata file when they quit. If a player is directly or indirectly
     * riding a non-persistent entity, the vehicle at the root and all its
     * passengers won't get persisted.
     * <p>
     * <b>This should not be confused with
     * {@link LivingEntity#setRemoveWhenFarAway(boolean)} which controls
     * despawning of living entities. </b>
     *
     * @return true if this entity is persistent
     * @deprecated draft API
     */
    @Override
    public boolean isPersistent() {
        return false;
    }

    /**
     * Sets whether or not the entity gets persisted.
     *
     * @param persistent the persistence status
     * @see #isPersistent()
     * @deprecated draft API
     */
    @Override
    public void setPersistent(boolean persistent) {

    }

    @Override
    public Entity getPassenger() {
        return null;
    }

    @Override
    public boolean setPassenger(Entity passenger) {
        return false;
    }

    @Override
    public List<Entity> getPassengers() {
        return null;
    }

    @Override
    public boolean addPassenger(Entity passenger) {
        return false;
    }

    @Override
    public boolean removePassenger(Entity passenger) {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean eject() {
        return false;
    }

    @Override
    public float getFallDistance() {
        return 0;
    }

    @Override
    public void setFallDistance(float distance) {

    }

    @Override
    public void setLastDamageCause(EntityDamageEvent event) {

    }

    @Override
    public EntityDamageEvent getLastDamageCause() {
        return null;
    }

    @Override
    public UUID getUniqueId() {
        if (isOnline()) {
            return player.getUniqueId();
        } else return offlinePlayer.getUniqueId();
    }

    @Override
    public int getTicksLived() {
        return 0;
    }

    @Override
    public void setTicksLived(int value) {

    }

    @Override
    public void playEffect(EntityEffect type) {

    }

    @Override
    public EntityType getType() {
        return EntityType.PLAYER;
    }

    @Override
    public boolean isInsideVehicle() {
        return false;
    }

    @Override
    public boolean leaveVehicle() {
        return false;
    }

    @Override
    public Entity getVehicle() {
        return null;
    }

    @Override
    public void setCustomNameVisible(boolean flag) {

    }

    @Override
    public boolean isCustomNameVisible() {
        return false;
    }

    @Override
    public void setGlowing(boolean flag) {

    }

    @Override
    public boolean isGlowing() {
        return false;
    }

    @Override
    public void setInvulnerable(boolean flag) {

    }

    @Override
    public boolean isInvulnerable() {
        return false;
    }

    @Override
    public boolean isSilent() {
        return false;
    }

    @Override
    public void setSilent(boolean flag) {

    }

    @Override
    public boolean hasGravity() {
        return false;
    }

    @Override
    public void setGravity(boolean gravity) {

    }

    @Override
    public int getPortalCooldown() {
        return 0;
    }

    @Override
    public void setPortalCooldown(int cooldown) {

    }

    @Override
    public Set<String> getScoreboardTags() {
        return null;
    }

    @Override
    public boolean addScoreboardTag(String tag) {
        return false;
    }

    @Override
    public boolean removeScoreboardTag(String tag) {
        return false;
    }

    @Override
    public PistonMoveReaction getPistonMoveReaction() {
        return null;
    }

    /**
     * Get the closest cardinal {@link BlockFace} direction an entity is
     * currently facing.
     * <br>
     * This will not return any non-cardinal directions such as
     * {@link BlockFace#UP} or {@link BlockFace#DOWN}.
     * <br>
     * {@link Hanging} entities will override this call and thus their behavior
     * may be different.
     *
     * @return the entity's current cardinal facing.
     * @see Hanging
     */
    @Override
    public @NotNull BlockFace getFacing() {
        return BlockFace.NORTH_EAST;
    }

    @Override
    public @NotNull Pose getPose() {
        return Pose.STANDING;
    }

    @Override
    public String getCustomName() {
        if (isOnline()) {
            return player.getName();
        }
        return offlineName;
    }

    @Override
    public void setCustomName(String name) {
        if (isOnline()) {
            player.setCustomName(name);
        } else sendError();
    }

    @Override
    public void setMetadata(String metadataKey, MetadataValue newMetadataValue) {

    }

    @Override
    public List<MetadataValue> getMetadata(String metadataKey) {
        return null;
    }

    @Override
    public boolean hasMetadata(String metadataKey) {
        return false;
    }

    @Override
    public void removeMetadata(String metadataKey, Plugin owningPlugin) {

    }

    @Override
    public boolean isPermissionSet(String name) {
        return false;
    }

    @Override
    public boolean isPermissionSet(Permission perm) {
        return false;
    }

    @Override
    public boolean hasPermission(String name) {
        if (isOnline()) {
            return player.hasPermission(name);
        } else return permission.playerHas(Bukkit.getServer().getWorlds().get(0).getName(), offlinePlayer, name);
    }

    @Override
    public boolean hasPermission(Permission perm) {
        if (isOnline()) {
            return player.hasPermission(perm.getName());
        } else
            return permission.playerHas(Bukkit.getServer().getWorlds().get(0).getName(), offlinePlayer, perm.getName());
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value) {
        return null;
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin) {
        return null;
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value, int ticks) {
        return null;
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, int ticks) {
        return null;
    }

    @Override
    public void removeAttachment(PermissionAttachment attachment) {
        //Empty
    }

    @Override
    public void recalculatePermissions() {

    }

    @Override
    public Set<PermissionAttachmentInfo> getEffectivePermissions() {
        if (isOnline()) {
            return player.getEffectivePermissions();
        } else return new HashSet<PermissionAttachmentInfo>(); //TODO ДОДЕЛАТЬ

    }

    @Override
    public boolean isOp() {
        if (isOnline()) {
            return player.isOp();
        }
        return offlinePlayer.isOp();
    }

    @Override
    public void setOp(boolean value) {
        if (isOnline()) {
            player.setOp(value);
        }
        offlinePlayer.setOp(value);
    }

    @Override
    public void sendPluginMessage(Plugin source, String channel, byte[] message) {
        if (isOnline()) {
            player.sendPluginMessage(source, channel, message);
        } else sendError();
    }

    @Override
    public Set<String> getListeningPluginChannels() {
        if (isOnline()) {
            return player.getListeningPluginChannels();
        }
        return null;
    }

    @Override
    public <T extends Projectile> T launchProjectile(Class<? extends T> projectile) {
        return null;
    }

    @Override
    public <T extends Projectile> T launchProjectile(Class<? extends T> projectile, Vector velocity) {
        return null;
    }

    @Override
    public @NotNull PersistentDataContainer getPersistentDataContainer() {
        return null;
    }
}
