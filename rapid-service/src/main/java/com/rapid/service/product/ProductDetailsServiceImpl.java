package com.rapid.service.product;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;


@Service
@Slf4j
public class ProductDetailsServiceImpl implements ProductDetailsService{
//
//    @Autowired
//    private ProductDetailsRepository productDetailsRepository;
//
//    @Autowired
//    private ProductMapper productMapper;
//    @Override
//    public ProductDTO createProduct(ProductDTO productDTO) {
//        ProductDetails product = productMapper.productDTOToProduct(productDTO);
//        ProductDetails savedProduct = productDetailsRepository.saveAndFlush(product);
//        return productMapper.productToProductDTO(savedProduct);
//    }
//
//    @Override
//    public ProductDTO getProductById(Integer id) {
//        ProductDetails product = productDetailsRepository.findById(id)
//                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));
//        return productMapper.productToProductDTO(product);
//    }
//
//    @Override
//    public List<ProductDTO> getAllProducts() {
//        return productDetailsRepository.findAll().stream()
//                .map(productMapper::productToProductDTO)
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public List<ProductDTO> searchProducts(String keyword) {
//        return productDetailsRepository.findByNameContainingIgnoreCase(keyword).stream()
//                .map(productMapper::productToProductDTO)
//                .collect(Collectors.toList());
//    }
}
