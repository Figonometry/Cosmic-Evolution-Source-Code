package spacegame.entity;

public interface IDecayable { //Should generally only go on living entities

    void destroyOnDecay();

    long getDecayTime();
}
