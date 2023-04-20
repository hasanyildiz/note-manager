package org.manager.note.controls;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.File;

@Builder
@AllArgsConstructor
public @Data class TabInfo {
    private String tabId;
    private File relatedFile;
    private String tabHeader;
    private byte[] fileHash;
}
