package de.bht.jvr.portals.tests.physics;

import javax.vecmath.Matrix4f;

import com.bulletphysics.linearmath.MotionState;

import de.bht.jvr.core.SceneNode;
import de.bht.jvr.core.Transform;
import de.bht.jvr.math.Matrix4;

/**
 * 
 * Source: jVR-Framework
 *
 */
public class MyMotionState implements MotionState
{
    private SceneNode node;

    public MyMotionState(SceneNode node)
    {
        this.node = node;
    }

    @Override
    public com.bulletphysics.linearmath.Transform getWorldTransform(com.bulletphysics.linearmath.Transform out)
    {
        float[] t = node.getTransform().getMatrix().getData();
        out.set(new Matrix4f(t));
        return out;
    }

    @Override
    public void setWorldTransform(com.bulletphysics.linearmath.Transform worldTrans)
    {
        Matrix4f m = new Matrix4f();
        worldTrans.getMatrix(m);
        float[] newTrans = new float[]{m.m00, m.m01, m.m02, m.m03,
                                       m.m10, m.m11, m.m12, m.m13,
                                       m.m20, m.m21, m.m22, m.m23,
                                       m.m30, m.m31, m.m32, m.m33};
        node.setTransform(new Transform(new Matrix4(newTrans)));
    }
}
