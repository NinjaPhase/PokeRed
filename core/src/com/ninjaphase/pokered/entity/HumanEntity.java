package com.ninjaphase.pokered.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.ninjaphase.pokered.data.map.TileMap;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *     The {@code HumanEntity} is used to portray an entity which uses human movement.
 * </p>
 */
public class HumanEntity extends Entity {
    private static final float MOVE_SPEED = 64.0f, ANIM_DURATION = 0.15f;
    private static final int GRID = 16;

    private final Vector2 velocity;

    private int targetX, targetY;
    private float animTimer;

    Animation<TextureRegion>[] animations;

    private EntityDirection moveDirection, lookDirection, nextMoveDirection;

    private List<HumanMoveFinishEvent> onMoves;

    /**
     * <p>
     *     Constructs a new {@code HumanEntity}.
     * </p>
     *
     * @param texture The texture.
     * @param map The map.
     * @param x The x position.
     * @param y The y position.
     */
    public HumanEntity(Texture texture, TileMap map, int x, int y) {
        super(map, x, y);
        this.onMoves = new ArrayList<>();
        this.velocity = new Vector2();
        this.lookDirection = EntityDirection.DOWN;
        this.targetX = this.tileX;
        this.targetY = this.tileY;
        if(texture != null) {
            int pWidth = texture.getWidth()/4;
            int pHeight = texture.getHeight()/4;
            this.animations = new Animation[4];
            for (int i = 0; i < this.animations.length; i++) {
                this.animations[i] = new Animation<>(ANIM_DURATION,
                        new TextureRegion(texture, 0, i * pHeight, pWidth, pHeight),
                        new TextureRegion(texture, 16, i * pHeight, pWidth, pHeight),
                        new TextureRegion(texture, 32, i * pHeight, pWidth, pHeight),
                        new TextureRegion(texture, 48, i * pHeight, pWidth, pHeight)
                );
            }
        }
    }

    /**
     * <p>
     *     Attempts to move the player in a given direction.
     * </p>
     *
     * @param nextMove The next move direction of the player.
     */
    public void move(EntityDirection nextMove) {
        this.nextMoveDirection = nextMove;
        if(this.moveDirection == null && this.nextMoveDirection != null)
            this.lookDirection = this.nextMoveDirection;
    }

    /**
     * <p>
     *     Updates the player.
     * </p>
     *
     * @param deltaTime The delta time.
     */
    public void update(float deltaTime) {
        this.position.mulAdd(this.velocity, deltaTime);

        if(this.nextMoveDirection == EntityDirection.UP
                && moveDirection == null && canMove(tileX, tileY+1)) {
            moveDirection = lookDirection = EntityDirection.UP;
            targetY = tileY + 1;
            velocity.y = MOVE_SPEED;
        } else if(this.nextMoveDirection == EntityDirection.DOWN
                && moveDirection == null && canMove(tileX, tileY-1)) {
            moveDirection = lookDirection = EntityDirection.DOWN;
            targetY = tileY - 1;
            velocity.y = -MOVE_SPEED;
        } else if(this.nextMoveDirection == EntityDirection.LEFT
                && moveDirection == null && canMove(tileX-1, tileY)) {
            moveDirection = lookDirection = EntityDirection.LEFT;
            targetX = tileX - 1;
            velocity.x = -MOVE_SPEED;
        } else if(this.nextMoveDirection == EntityDirection.RIGHT
                && moveDirection == null && canMove(tileX+1, tileY)) {
            moveDirection = lookDirection = EntityDirection.RIGHT;
            targetX = tileX + 1;
            velocity.x = MOVE_SPEED;
        } else if(moveDirection != null) {
            this.animTimer += deltaTime;
            if(this.animTimer > this.animations[0].getAnimationDuration())
                this.animTimer -= this.animations[0].getAnimationDuration();
            if(moveDirection == EntityDirection.UP
                    && position.y >= targetY*GRID) {
                this.tileY = targetY;
                if(!this.hasTriggeredStep(tileX, tileY) && this.nextMoveDirection == EntityDirection.UP
                        && canMove(tileX, tileY+1)) {
                    this.targetY = this.tileY+1;
                } else {
                    this.moveDirection = null;
                    this.velocity.y = 0.0f;
                    this.position.y = tileY * GRID;
                    this.animTimer = this.animTimer > ANIM_DURATION*2.0f ? 0.0f : ANIM_DURATION*2.0f;
                }
            } else if(moveDirection == EntityDirection.DOWN
                    && position.y <= targetY*GRID) {
                this.tileY = targetY;
                if(!this.hasTriggeredStep(tileX, tileY) && this.nextMoveDirection == EntityDirection.DOWN
                        && canMove(tileX, tileY-1)) {
                    this.targetY = this.tileY-1;
                } else {
                    this.moveDirection = null;
                    this.velocity.y = 0.0f;
                    this.position.y = tileY * GRID;
                    this.animTimer = this.animTimer > ANIM_DURATION*2.0f ? 0.0f : ANIM_DURATION*2.0f;
                }
            } else if(moveDirection == EntityDirection.LEFT
                    && position.x <= targetX*GRID) {
                this.tileX = targetX;
                if(!this.hasTriggeredStep(tileX, tileY) && this.nextMoveDirection == EntityDirection.LEFT
                        && canMove(tileX-1, tileY)) {
                    this.targetX = this.tileX-1;
                } else {
                    this.moveDirection = null;
                    this.velocity.x = 0.0f;
                    this.position.x = tileX * GRID;
                    this.animTimer = this.animTimer > ANIM_DURATION*2.0f ? 0.0f : ANIM_DURATION*2.0f;
                }
            } else if(moveDirection == EntityDirection.RIGHT
                    && position.x >= targetX*GRID) {
                this.tileX = targetX;
                if(!this.hasTriggeredStep(tileX, tileY) && this.nextMoveDirection == EntityDirection.RIGHT
                        && canMove(tileX+1, tileY)) {
                    this.targetX = this.tileX+1;
                } else {
                    this.moveDirection = null;
                    this.velocity.x = 0.0f;
                    this.position.x = tileX * GRID;
                    this.animTimer = this.animTimer > ANIM_DURATION*2.0f ? 0.0f : ANIM_DURATION*2.0f;
                }
            }
        }
    }

    /**
     * <p>
     *     Renders the player.
     * </p>
     *
     * @param batch The batch to render to.
     */
    public void render(SpriteBatch batch) {
        if(this.animations != null) {
            batch.draw(this.animations[this.lookDirection.ordinal()].getKeyFrame(this.animTimer),
                    this.position.x, this.position.y);
        }
    }

    /**
     * <p>
     *     Whether the step has been triggered.
     * </p>
     * @param x The x position.
     * @param y The y position.
     * @return Whether it has been activated.
     */
    private boolean hasTriggeredStep(int x, int y) {
        boolean triggered = false;
        for(HumanMoveFinishEvent ev : this.onMoves) {
            triggered = triggered || ev.onMoveStep(this, x, y);
            if(triggered)
                break;
        }
        return triggered;
    }

    @Override
    public void setMap(TileMap map, int x, int y) {
        super.setMap(map, x, y);
        this.targetX = x;
        this.targetY = y;
    }

    /**
     * <p>
     *     Adds a move listener.
     * </p>
     *
     * @param ev The event.
     */
    public void addMoveListener(HumanMoveFinishEvent ev) {
        this.onMoves.add(ev);
    }

    /**
     * <p>
     *     Checks collisions on whether the user can move.
     * </p>
     *
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @return Whether the users can move.
     */
    boolean canMove(int x, int y) {
        return x >= 0 && x < this.map.getWidth() && y >= 0 && y < this.map.getHeight() && !this.map.getCollision(x, y);
    }

    /**
     * @return Whether the player is moving.
     */
    public boolean isMoving() {
        return (tileX != targetX || tileY != targetY);
    }

    /**
     * @return The direction the entity is looking in.
     */
    public EntityDirection getLookDirection() {
        return lookDirection;
    }
}
