# Plan: Fix MC 1.17 → 26.2 Migration Errors
This mod is ~90% ported to Minecraft 26.2 (Mojmap/Fabric). The remaining ~100 compile errors reduce to a handful of root causes — most are cascade failures from 4 relocated classes + 1 renamed Fabric package. Fix the roots first, then the mechanical variable renames, then the per-callsite API changes (render refactor last).

Key finding: build.gradle has no explicit mappings line, but nearly all Mojmap classes resolve fine — so mappings are applied (loom default). The 4 unresolved classes are genuinely relocated/renamed in 26.2, not a config problem. No mappings changes needed.

## [X] Phase 1 — Verify new API locations (research only)
Confirm the 26.2 targets before editing, using the fabric-api-0.153.0+26.2-fatjavadoc.jar and official Mojmap 26.2 docs:

New packages for `ResourceLocation`, `GuiGraphics`, `LevelAccessor`, `WorldlyContainerHolder`
New name/package for `ExtendedScreenHandlerFactory` in fabric-api 0.153.0+26.2
The 26.2 GuiGraphics.blit(...) signature + replacement for RenderSystem.setShaderColor / Screen::applyBlitOffset
## [ ] Phase 2 — Fix relocated classes & Fabric package (clears most cascades)
1. Update imports for the 4 relocated classes across ~15 files (screens, tile entities, `Reference`, `IronFurnaces`, `IronFurnacesClient`).
2. Fix the `ExtendedScreenHandlerFactory` package in `BlockIronFurnaceTileBase.java:68` and `BlockWirelessHeaterTile.java:31`. This clears the `@Override getScreenOpeningData` errors and most `GuiGraphics`-related `@Override` cascades.
3. Recompile to confirm cascade count drops sharply.

## [ ] Phase 3 — Mechanical variable fixes
4. `world` → `this.level` in `BlockIronFurnaceTileBase.java:375` (lines 375–456, `autoIO()`/`getInventoryAt` scope).
5. Bare handler / this.handler → this.menu throughout `BlockIronFurnaceScreenBase.java:67` (Mojmap `AbstractContainerScreen` field is `menu`).

## [ ] Phase 4 — Per-callsite API changes
6. `Component.CODEC` → `ComponentSerialization.CODEC` (`TileEntityInventory.java:116` lines 116, 125).
7. `BlockPos.ORIGIN` → `BlockPos.ZERO` (`BlockWirelessHeaterScreenHandler.java:47`).
8. `item.hasCraftingRemainingItem()`/`getCraftingRemainingItem()` → component-based remainder (line 321).
9. `recipe.id().location()` → use `recipe.id()` (now `ResourceKey<Recipe<?>>`) adapting the `recipesUsed` key handling (line 886); also `ResourceLocation.tryParse` usage at `BlockIronFurnaceTileBase.java:780`.
10. `ChestBlock.getInventory(...)` signature (line 560).
11. `stack.onCrafted(int)` removed (`SlotIronFurnace.java:41`).
12. `player.displayClientMessage(...)` — verify Player import/type (`BlockIronFurnaceBase.java:215`).
13. Render refactor (hardest): rewrite `guiGraphics.blit(Screen::applyBlitOffset, ...)` calls and drop `RenderSystem.setShaderColor` across `BlockIronFurnaceScreenBase.java:345` and `BlockWirelessHeaterScreen.java:58` per the confirmed 26.2 `GuiGraphics` API.

## Relevant files

`BlockIronFurnaceTileBase.java` — imports, `world`→`level`, recipe id, chest inventory, crafting remainder, Fabric factory
`BlockIronFurnaceScreenBase.java` — GuiGraphics/ResourceLocation imports, `handler`→`menu`, blit refactor
`BlockWirelessHeaterScreen.java`, `BlockWirelessHeaterTile.java`, `BlockWirelessHeaterScreenHandler.java`
`TileEntityInventory.java`, `SlotIronFurnace.java`, `Reference.java`, `IronFurnaces.java`, `IronFurnacesClient.java`
The 8 per-block furnace screen files (each just needs the `ResourceLocation` import fixed)

## Verification

`.gradlew.bat compileJava` → BUILD SUCCESSFUL, 0 errors (run iteratively after each phase to watch the count drop).
Confirm render/blit edits match the real 26.2 GuiGraphics.blit signature (verified in Phase 1, not guessed).
`.gradlew.bat build` to confirm remap + jar succeed.


## Decisions

Included: all compile-error fixes across the listed files. Excluded: `ironfurnaces/rei/**` (excluded in `build.gradle`) and any runtime/gameplay testing beyond compilation.
No mappings/build.gradle changes (mappings confirmed working).