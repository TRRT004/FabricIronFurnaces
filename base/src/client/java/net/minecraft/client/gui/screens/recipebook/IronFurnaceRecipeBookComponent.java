package net.minecraft.client.gui.screens.recipebook;

import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.inventory.RecipeBookMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.FurnaceRecipeDisplay;
import net.minecraft.util.context.ContextMap;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import java.util.List;

public class IronFurnaceRecipeBookComponent extends RecipeBookComponent<RecipeBookMenu> {
	private static final WidgetSprites FILTER_SPRITES = new WidgetSprites(
		Identifier.withDefaultNamespace("recipe_book/furnace_filter_enabled"),
		Identifier.withDefaultNamespace("recipe_book/furnace_filter_disabled"),
		Identifier.withDefaultNamespace("recipe_book/furnace_filter_enabled_highlighted"),
		Identifier.withDefaultNamespace("recipe_book/furnace_filter_disabled_highlighted")
	);

	// Suppress generic type warnings because RecipeBookComponent super constructor expects List<TabInfo> (parameterized by parent T type parameters) which causes wildcard capture mismatches in multi-project builds
	@SuppressWarnings({"rawtypes", "unchecked"})
	public IronFurnaceRecipeBookComponent(RecipeBookMenu menu, List tabs) {
		super(menu, tabs);
	}

	@Override
	protected void selectMatchingRecipes(RecipeCollection collection, StackedItemContents contents) {
		collection.selectRecipes(contents, (display) -> display instanceof FurnaceRecipeDisplay);
	}

	@Override
	protected boolean isCraftingSlot(Slot slot) {
		return slot.index == 0 || slot.index == 2;
	}

	@Override
	protected Component getRecipeFilterName() {
		return Component.translatable("gui.recipebook.toggleRecipes.smeltable");
	}

	@Override
	protected WidgetSprites getFilterButtonTextures() {
		return FILTER_SPRITES;
	}

	@Override
	protected void fillGhostRecipe(GhostSlots ghostSlots, RecipeDisplay recipeDisplay, ContextMap contextMap) {
		if (recipeDisplay instanceof FurnaceRecipeDisplay furnaceRecipeDisplay) {
			ghostSlots.setInput(this.menu.getSlot(0), contextMap, furnaceRecipeDisplay.ingredient());
			Slot fuelSlot = this.menu.getSlot(1);
			if (fuelSlot.getItem().isEmpty()) {
				ghostSlots.setInput(fuelSlot, contextMap, furnaceRecipeDisplay.fuel());
			}
		}
	}
}
