  private final HashMap<Player, List<ItemStack>> itemBlacklist = new HashMap<>();
	private final HashMap<Arena, List<Player>> playerArena = new HashMap<>();
	
	public List<ItemStack> getItemBlacklist(Player p) {
		List<ItemStack> itemsList = new LinkedList<>();
		for (ItemStack i : p.getInventory().getContents()) {
			if (i == null || i.getType() == Material.AIR) { continue; }
			
			if (i.hasItemMeta()) {
				//Display name item 1
				if (i.getItemMeta().getDisplayName().equals("Display Name Item One")) {
					itemsList.add(i);
					p.getInventory().remove(i);
				}
				//Display name item 2
				if (i.getItemMeta().getDisplayName().equals("Display Name Item Two")) {
					itemsList.add(i);
					p.getInventory().remove(i);
				}
				// And more ...
				
				if (i.getItemMeta().hasLore()) {
					// Lore 1
					if (i.getItemMeta().getLore().contains("Lore Item 1")) {
						itemsList.add(i);
						p.getInventory().remove(i);
					}
					//Lore 2
					if (i.getItemMeta().getLore().contains("Lore Item 2")) {
						itemsList.add(i);
						p.getInventory().remove(i);
					}
					// And more ...
				}
			}
		}
		
		return itemsList;
	}
	
	@EventHandler
	public void onJoinArena(PlayerJoinArenaEvent event) {
		Player p = event.getPlayer();
		Arena arena = event.getArena();
		
		if (arena.withoutInventory()) {	
			List<ItemStack> items = getItemBlacklist(p);
			if (!items.isEmpty() && items != null) {
				itemBlacklist.put(p, getItemBlacklist(p));
			}
		}
	}
	
	@EventHandler
	public void onArenaStart(ArenaStartEvent event) {
		Arena arena = event.getArena();
		
		if (arena.withoutInventory()) {
			for (Player p : arena.getPlayers()) {
				if (itemBlacklist.containsKey(p)) {
					List<ItemStack> itemsList = itemBlacklist.get(p);
					itemsList.forEach(i -> p.getInventory().addItem(i));
				}
			}
		}
	}
	
	@EventHandler
	public void onArenaEnd(ArenaEndEvent event) {
		Arena arena = event.getArena();
		
		if (arena.withoutInventory()) {
			List<Player> playerList = new LinkedList<>();
			for (Player p : arena.getPlayers()) {
				List<ItemStack> items = getItemBlacklist(p);
				if (!items.isEmpty() && items != null) {
					itemBlacklist.put(p, getItemBlacklist(p));
					playerList.add(p);
				}
			}
			
			if (!playerList.isEmpty() && playerList != null) {
				playerArena.put(arena, playerList);
			}
		}
	}
	
	@EventHandler
	public void onArenaRestart(ArenaRestartEvent event) {
		Arena arena = event.getArena();
		
		if (arena.withoutInventory()) {
			if (playerArena.containsKey(arena)) {
				List<Player> playerList = playerArena.get(arena);
				for (Player p : playerList) {
					if (itemBlacklist.containsKey(p)) {
						List<ItemStack> itemsList = itemBlacklist.get(p);
						itemsList.forEach(i -> p.getInventory().addItem(i));
					}
				}
			}
		}
	}
