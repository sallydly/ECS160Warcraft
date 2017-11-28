package com.warcraftII.player_asset;

import com.warcraftII.GameDataTypes;
import com.warcraftII.data_source.DataSource;
import com.warcraftII.player_asset.ETargetType;
import com.warcraftII.player_asset.PlayerData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.xml.crypto.Data;

import static com.warcraftII.GameDataTypes.to_underlying;

abstract class ActivatedPlayerCapability {
    protected PlayerAsset actor;
    protected PlayerData playerData;
    protected PlayerAsset target;

    public ActivatedPlayerCapability(PlayerAsset actor, PlayerData playerData, PlayerAsset target) {
        this.actor = actor;
        this.playerData = playerData;
        this.target = target;
    }

    public abstract int percentComplete(int max);
    public abstract boolean incrementStep();
    public abstract void cancel();
}


//TODO: Move to separate file
abstract class DataContainerIterator{
    public abstract String name();
    public abstract boolean isContainer();
    public abstract boolean isValid();
    public abstract void next();
}

abstract class DataSink {
    public abstract int write(Object data, int length);
    public DataContainer container() {
        return null;
    }
}

abstract class DataContainer {
    public abstract DataContainerIterator first();
    public abstract DataSource DataSource(String name);
    public abstract DataSink dataSink(String name);
    public DataContainer container() {
        return null;
    }

    public abstract DataContainer dataContainer(String name);
}

public class PlayerAsset {

}