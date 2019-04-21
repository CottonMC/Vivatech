package io.github.cottonmc.um;

import net.minecraft.util.math.BoundingBox;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class VoxelMath {
	public static BoundingBox rotate(BoundingBox bb, Direction dir) {
		Vec3d point1 = new Vec3d(bb.minX, bb.minY, bb.minZ);
		Vec3d point2 = new Vec3d(bb.maxX, bb.maxY, bb.maxZ);
		
		switch(dir) {
		case NORTH:
			break;
		case EAST:
			point1 = rotateHorizontal(point1, 90);
			point2 = rotateHorizontal(point2, 90);
			break;
		case SOUTH:
			point1 = rotateHorizontal(point1, 180);
			point2 = rotateHorizontal(point2, 180);
			break;
		case WEST:
			point1 = rotateHorizontal(point1, 270);
			point2 = rotateHorizontal(point2, 270);
			break;
		default:
			break;
		}
		
		double minX = Math.min(point1.getX(), point2.getX());
		double minY = Math.min(point1.getY(), point2.getY());
		double minZ = Math.min(point1.getZ(), point2.getZ());
		
		double maxX = Math.max(point1.getX(), point2.getX());
		double maxY = Math.max(point1.getY(), point2.getY());
		double maxZ = Math.max(point1.getZ(), point2.getZ());
		
		return new BoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
	}
	
	
	public static Vec3d rotateHorizontal(Vec3d v, int degrees) {
		return rotateHorizontal(v.getX(), v.getY(), v.getZ(), degrees);
	}
	
	public static Vec3d rotateHorizontal(double x, double y, double z, int degrees) {
		double dx = x-0.5;
		double dz = z-0.5;
		double theta = Math.atan2(dz, dx);
		double r = Math.sqrt(dx*dx + dz*dz);
		
		theta += (degrees * (Math.PI/180.0));
		
		double xp = Math.cos(theta)*r + 0.5;
		double zp = Math.sin(theta)*r + 0.5;
		
		return new Vec3d(xp, y, zp);
	}
}
