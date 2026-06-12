package com.Safe.Link.controller;

import com.Safe.Link.DTO.ItemKitDTO;
import com.Safe.Link.service.ItemKitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/items-kit")
@RequiredArgsConstructor
public class ItemKitController {
    private final ItemKitService service;

    @GetMapping("/kit/{idKit}")
    public ResponseEntity<List<ItemKitDTO>>getByKit(@PathVariable Long idKit){
        return ResponseEntity.ok(service.getByKit(idKit));
    }

    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<ItemKitDTO>>getByCategoria(@PathVariable String categoria){
       return ResponseEntity.ok(service.getByCategoria(categoria));
    }

    @PostMapping
    public ResponseEntity<ItemKitDTO>create(@RequestBody ItemKitDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ItemKitDTO> update(@PathVariable Long id, @RequestBody ItemKitDTO dto){
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void>delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

}
