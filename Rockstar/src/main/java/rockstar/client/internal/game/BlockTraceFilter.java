package rockstar.client.internal.game;


import rockstar.client.*;
import net.minecraft.block.BlockState;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.TrapdoorBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.RaycastContext.ShapeType;

public enum BlockTraceFilter {
   internalField0305,
   internalField0304,
   internalField1119,
   internalField1117,
   internalField1118;

   public boolean internalMethod01474(BlockView localValue1, BlockPos localValue2, BlockState localValue3) {
      return switch (this) {
         case internalField0305 -> false;
         case internalField0304, internalField1118 -> true;
         case internalField1119 -> localValue3.getBlock() instanceof DoorBlock || localValue3.getBlock() instanceof TrapdoorBlock;
         case internalField1117 -> !localValue3.isFullCube(localValue1, localValue2);
      };
   }

   public ShapeType internalMethod07638() {
      return this == internalField1119 ? ShapeType.OUTLINE : ShapeType.COLLIDER;
   }

   public boolean internalMethod04282() {
      return this == internalField0304 || this == internalField1118;
   }
}
