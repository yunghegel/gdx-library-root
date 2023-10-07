package ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;

public class STree<N extends Tree.Node, V> extends Tree {

    public STree(Skin skin) {
        super(skin);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        getRootNodes().forEach(node -> {
           drawSelection((Node) node, getStyle().over, batch, getX(), getY(), getWidth(), getHeight());
        });
    }

    @Override
    protected float drawIcons(Batch batch, float r, float g, float b, float a, Node parent, Array nodes, float indent, float plusMinusWidth) {

        Rectangle cullingArea = getCullingArea();
        float cullBottom, cullTop;
        if (cullingArea != null) {
            cullBottom = cullingArea.y;
            cullTop = cullBottom + cullingArea.height;
        } else {
            cullTop = 0;
            cullBottom = 0;
        }
        nodes.forEach(node -> {
            Actor actor = ((Node) node).getActor();
            if (cullingArea == null || (actor.getY() + actor.getHeight() >= cullBottom && actor.getY() <= cullTop)) {
                if (getSelection().contains(node) && getStyle().selection != null) {
                    drawSelection((Node) node, getStyle().selection, batch, getX(), getY() + actor.getY() - getYSpacing() / 2, getWidth(), actor.getHeight() + getYSpacing());
                } else if (node == getOverNode() && getStyle().over != null) {
                    drawOver((Node) node, getStyle().over, batch, getX(), getY() + actor.getY() - getYSpacing() / 2, getWidth(), actor.getHeight() + getYSpacing());
                }

                if (((Node) node).getIcon() != null) {
                    float iconY = getY() + actor.getY() + Math.round((actor.getHeight() - ((Node) node).getIcon().getMinHeight()) / 2);
                    Color actorColor = actor.getColor();
                    batch.setColor(actorColor.r, actorColor.g, actorColor.b, actorColor.a * a);
                    drawIcon((Node) node, ((Node) node).getIcon(), batch, indent + plusMinusWidth + getIndentSpacing(), iconY);
                    batch.setColor(r, g, b, a);
                }

                if (((Node) node).getChildren().size > 0) {
                    Drawable expandIcon = getExpandIcon((Node) node, indent + plusMinusWidth + getIndentSpacing());
                    float iconY = getY() + actor.getY() + Math.round((actor.getHeight() - expandIcon.getMinHeight()) / 2);
                    drawExpandIcon((Node) node, expandIcon, batch, indent, iconY);
                }
            } else if (actor.getY() < cullBottom) //
                return;
            if (((Node) node).isExpanded() && ((Node) node).getChildren().size > 0)
                drawIcons(batch, r, g, b, a, (Node) node, ((Node) node).getChildren(), indent + getIndentSpacing(), plusMinusWidth);
        });
        TreeStyle style = this.getStyle();
        float x = getX(), y = getY(), expandX = x + indent, iconX = expandX + plusMinusWidth + getIndentSpacing(), actorY = 0;
        for (int i = 0, n = nodes.size; i < n; i++) {
            N node = (N) nodes.get(i);
            Actor actor = node.getActor();
            actorY = actor.getY();
            float height = node.getHeight();
            if(i%2==0){
                drawSelection(node, style.selection, batch, x, y + actorY - getYSpacing() / 2, getWidth(), height + getYSpacing());
                drawOver(node, style.selection, batch, x, y + actorY - getYSpacing() / 2, getWidth(), height + getYSpacing());
            }

            if (cullingArea == null || (actorY + height >= cullBottom && actorY <= cullTop)) {
                if (getSelection().contains(node) && style.selection != null) {
                    drawSelection(node, style.selection, batch, x, y + actorY - getYSpacing() / 2, getWidth(), height + getYSpacing());
                } else if (node == getOverNode() && style.over != null) {
                    drawOver(node, style.over, batch, x, y + actorY - getYSpacing() / 2, getWidth(), height + getYSpacing());
                }

                if (node.getIcon() != null) {
                    float iconY = y + actorY + Math.round((height - node.getIcon().getMinHeight()) / 2);
                    Color actorColor = actor.getColor();
                    batch.setColor(actorColor.r, actorColor.g, actorColor.b, actorColor.a * a);
                    drawIcon(node, node.getIcon(), batch, iconX, iconY);
                    batch.setColor(r, g, b, a);
                }

                if (node.getChildren().size > 0) {
                    Drawable expandIcon = getExpandIcon(node, iconX);
                    float iconY = y + actorY + Math.round((height - expandIcon.getMinHeight()) / 2);
                    drawExpandIcon(node, expandIcon, batch, expandX, iconY);
                }
            } else if (actorY < cullBottom) //
                break;
            if (node.isExpanded() && node.getChildren().size > 0)
                drawIcons(batch, r, g, b, a, node, node.getChildren(), indent + getIndentSpacing(), plusMinusWidth);
        }
        return actorY;
    }

    public void drawOver(Node node, Batch batch, float x, float y, float width, float height) {
        drawOver(node, getStyle().selection, batch, x, y, width, height);
        drawSelection(node, getStyle().selection, batch, x, y, width, height);
    }

}

