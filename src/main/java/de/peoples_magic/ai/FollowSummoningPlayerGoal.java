package de.peoples_magic.ai;

import de.peoples_magic.entity.spells.SummonedEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.goal.FollowMobGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.player.Player;
import javax.annotation.Nullable;
import java.util.EnumSet;

public class FollowSummoningPlayerGoal extends FollowMobGoal {
    private Mob mob;
    private SummonedEntity summonedEntity;
    @Nullable
    private Player followingMob;
    private final double speedModifier;
    private final PathNavigation navigation;
    private int timeToRecalcPath;
    private final float stopDistance;
    private final Cow cow;

    /**
     * Constructs a goal allowing a mob to follow others. The mob must have Ground or Flying navigation.
     *
     * @param entity
     * @param speedModifier
     */
    public FollowSummoningPlayerGoal(SummonedEntity entity, double speedModifier) {
        super(((Mob)entity), speedModifier, 4, 0f);
        Mob m = ((Mob)entity);
        this.summonedEntity = entity;
        this.mob = m;
        this.speedModifier = speedModifier;
        this.navigation = mob.getNavigation();
        this.stopDistance = 4;
        this.cow = new Cow(EntityType.COW, mob.level());
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        if (!(mob.getNavigation() instanceof GroundPathNavigation) && !(mob.getNavigation() instanceof FlyingPathNavigation)) {
            throw new IllegalArgumentException("Unsupported mob type for FollowMobGoal");
        }
    }


    @Override
    public boolean canUse() {
        if (this.summonedEntity.get_owner() != null) {
            // try block?
            Player p = this.mob.level().getPlayerByUUID(this.summonedEntity.get_owner().getUUID());
            if (p != null) {
                this.followingMob = p;
                return true;
            }
        }
        return false;
    }

    @Override
    public void tick() {
        if (this.followingMob != null && !this.mob.isLeashed()) {
            this.mob.getLookControl().setLookAt(this.followingMob, 10.0F, (float)this.mob.getMaxHeadXRot());
            if (--this.timeToRecalcPath <= 0) {
                this.timeToRecalcPath = this.adjustedTickDelay(2);
                double d0 = this.mob.getX() - this.followingMob.getX();
                double d1 = this.mob.getY() - this.followingMob.getY();
                double d2 = this.mob.getZ() - this.followingMob.getZ();
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;
                if (!(d3 <= (double)(this.stopDistance * this.stopDistance))) {
                    this.navigation.moveTo(this.followingMob, this.speedModifier);
                } else {
                    this.navigation.stop();
                    LookControl lookcontrol = new LookControl(this.cow );
                    lookcontrol.setLookAt(this.mob);
                    if (d3 <= (double)this.stopDistance
                            || lookcontrol.getWantedX() == this.mob.getX()
                            && lookcontrol.getWantedY() == this.mob.getY()
                            && lookcontrol.getWantedZ() == this.mob.getZ()) {
                        double d4 = this.followingMob.getX() - this.mob.getX();
                        double d5 = this.followingMob.getZ() - this.mob.getZ();
                        this.navigation.moveTo(this.mob.getX() - d4, this.mob.getY(), this.mob.getZ() - d5, this.speedModifier);
                    }
                }
            }
        }
    }
}
